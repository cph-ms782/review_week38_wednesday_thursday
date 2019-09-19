package rest;

import dto.PersonDTO;
import entities.Person;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    //Read this line from a settings-file  since used several places
    private static final String TEST_DB = "jdbc:mysql://localhost:3307/person_test";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static Person p1, p2;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);

        //We are using the database on the virtual Vagrant image, so username password are the same for all dev-databases
        httpServer = startServer();

        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;

        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        p1 = new Person("a", "b", "c");
        p2 = new Person("d", "e", "f");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given()
                .when()
                .get("/person")
                .then()
                .statusCode(200);
    }
    
    @Test
    public void testPersonNotFoundException() {
        System.out.println("Testing PersonNotFoundException");
        given()
                .when()
                .get("/person/121233")
                .then()
                .statusCode(404);
    }
    
    @Test
    public void testGetPersonByID() throws Exception {
        System.out.println("Testing GetPersonByID");
        String getString = "/person/" + p1.getId();
        given()
                .contentType("application/json")
                .get(getString)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName", equalTo("a"));

    }

    @Test
    public void testGetAllPersons() {
        System.out.println("Testing GetAllPersons");
        List<PersonDTO> personsDTOs;
        personsDTOs = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/person/all")
                .then()
                .extract()
                .body()
                .jsonPath()
                .getList("all", PersonDTO.class);

        PersonDTO p1DTO = new PersonDTO(p1);
        PersonDTO p2DTO = new PersonDTO(p2);
        assertThat(personsDTOs, containsInAnyOrder(p1DTO, p2DTO));
    }

    @Test
    public void testPOST() {
        System.out.println("Testing POST - addPerson");
        given()
                .contentType(ContentType.JSON)
                .body(new PersonDTO("ib", "xxx", "123"))
                .when()
                .post("person")
                .then()
                .body("firstName", equalTo("ib"))
                .body("lastName", equalTo("xxx"))
                .body("id", notNullValue());
    }

    @Test
    public void testPUT() {
        System.out.println("Testing PUT - editPerson");
        String getString = "person/" + p1.getId();
        given()
                .contentType(ContentType.JSON)
                .body(new PersonDTO("ib", "xxx", "123"))
                .when()
                .put(getString)
                .then()
                .body("firstName", equalTo("ib"))
                .body("lastName", equalTo("xxx"))
                .body("id", notNullValue());
    }

    @Test
    public void testDELETE() {
        System.out.println("Testing DELETE - deletePerson");
        String getString = "person/" + p1.getId();
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(getString)
                .then()
                .body("Status", equalTo("Person deleted"));

    }
}
