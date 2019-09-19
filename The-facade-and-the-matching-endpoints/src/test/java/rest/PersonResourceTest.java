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
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.CREATE);

        //NOT Required if you use the version of EMF_Creator.createEntityManagerFactory used above        
        //System.setProperty("IS_TEST", TEST_DB);
        //We are using the database on the virtual Vagrant image, so username password are the same for all dev-databases
        httpServer = startServer();

        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;

        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.persist(new Person("firstName", "lastName", "phone", LocalDate.of(2000, Month.JANUARY, 1)));

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

//    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/api").then().statusCode(200);
    }

//    @Test
    public void testId() throws Exception {
        given()
                .contentType("application/json")
                .get("/api/person/id/1").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("data.firstName", equalTo("Hans"));
    }

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
