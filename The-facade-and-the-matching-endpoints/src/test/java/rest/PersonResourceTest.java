package rest;

import entities.Person;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.time.LocalDate;
import java.time.Month;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import static org.glassfish.grizzly.http.util.Header.ContentType;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
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
        p1 = new Person("firstName", "lastName", "phone");
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

//    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given()
                .when()
                .get("/api/person")
                .then()
                .statusCode(200);
    }

//    @Test
    public void testId() throws Exception {
        given()
                .contentType("application/json")
                .get("/api/person/1").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName", equalTo("firstName"));
    }

    private static String payload = "{\n"
            + "  \"description\": \"Some Description\",\n"
            + "  \"id\": \"Some id\",\n"
            + "  \"name\": \"Some name\"\n"
            + "}";

//    @Test
//    public void testPOST() throws Exception {
//        given()
//                .contentType(ContentType.)
//                .body(payload)
//                .post("/some/resource")
//                .then()
//                .statusCode(200)
//                .extract()
//                .response();
//    }

//    @Test
//    public void testDummyMsg() throws Exception {
//        given()
//        .contentType("application/json")
//        .get("/api/person").then()
//        .assertThat()
//        .statusCode(HttpStatus.OK_200.getStatusCode())
//        .body("msg", equalTo("Hello World"));   
//    }
//    @Test
//    public void testCount() throws Exception {
//        given()
//        .contentType("application/json")
//        .get("/api/person").then()
//        .assertThat()
//        .statusCode(HttpStatus.OK_200.getStatusCode())
//        .body("count", equalTo(2));   
//    }
}
