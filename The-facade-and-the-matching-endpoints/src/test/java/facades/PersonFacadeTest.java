package facades;

import utils.EMF_Creator;
import entities.Person;
import java.time.LocalDate;
import java.time.Month;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;

    private static Person p1 = new Person("a", "b", "c", LocalDate.of(2000, Month.JANUARY, 1));
    private static Person p2 = new Person("d", "e", "f", LocalDate.of(2010, Month.JULY, 15));

    public PersonFacadeTest() {
    }

    /*   **** HINT **** 
        A better way to handle configuration values, compared to the UNUSED example above, is to store those values
        ONE COMMON place accessible from anywhere.
        The file config.properties and the corresponding helper class utils.Settings is added just to do that. 
        See below for how to use these files. This is our RECOMMENDED strategy
     */
    @BeforeAll
    public static void setUpClassV2() {
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
        facade = PersonFacade.getPersonFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void testGetPerson() {
        System.out.println("Test getPerson:");
        assertEquals(p1, facade.getPerson(6), "Expects 2 persons to be the same");
    }

    @Test
    public void testGetAllPersons() {
        System.out.println("Test getAllPersons:");
        assertEquals(1, facade.getAllPersons().size(), "Expects a list of 1 person");
    }

    @Test
    public void testAddPerson() {
        System.out.println("Test addPerson:");
        facade.addPerson(
                p2.getFirstName(),
                p2.getLastName(),
                p2.getPhone());
        assertEquals(2, facade.getAllPersons().size(), "Expects a list of 2 persons");
    }

    @Test
    public void testDeletePerson() {
        System.out.println("Test deletePerson:");
        facade.deletePerson(2);
        assertEquals(0, facade.getAllPersons().size(), "Expects a list of 0 persons");
    }

    @Test
    public void testEditPerson() {
        System.out.println("Test editPerson:");
        assertEquals(1, facade.getAllPersons().size(), "Expects a list of 2 persons");
    }

}
