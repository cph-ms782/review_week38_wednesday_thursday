package facades;

import dto.PersonDTO;
import entities.Person;
import exceptions.PersonNotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class PersonFacade implements IPersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public Person addPerson(String fName, String lName, String phone) {
        Person person = new Person(fName, lName, phone);
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
            return person;
        } finally {
            em.close();
        }
    }

    @Override
    public Person deletePerson(int id) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Person p = em.find(Person.class, id);
            if (p == null) {
                throw new PersonNotFoundException("Could not delete, provided id does not exist");
            }
            Person NewP = new Person(p.getFirstName(), p.getLastName(), p.getPhone());
            em.remove(p);
            em.getTransaction().commit();
            return NewP;
        } finally {
            em.close();
        }
    }

    @Override
    public Person getPerson(int id) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            Person p = em.find(Person.class, id);
            if (p == null) {
                throw new PersonNotFoundException("No person with provided id found");
            }
            return p;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Person> getAllPersons() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> num = em.createQuery("Select c from Person c", Person.class);
            return num.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Person editPerson(Person p) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Person pOriginal = em.find(Person.class, p.getId());
            if (pOriginal == null) {
                throw new PersonNotFoundException("No person with provided id found");
            }
            pOriginal.setFirstName(p.getFirstName());
            pOriginal.setLastName(p.getLastName());
            pOriginal.setPhone(p.getPhone());
            em.getTransaction().commit();
            return pOriginal;
        } finally {
            em.close();
        }
    }

}
