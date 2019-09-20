package facades;

import entities.Customer;
import entities.ItemType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import utils.EMF_Creator;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class CustomerFacade {

    private static CustomerFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private CustomerFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static CustomerFacade getCustomerFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CustomerFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Create a Customer Find a Customer Get all Customers
     */
    public Customer addCustomer(String name, String email) {
        Customer c = new Customer(name, email);
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
            return c;
        } finally {
            em.close();
        }
    }

    public Customer findCustomer(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            Customer c = em.find(Customer.class, id);
            return c;
        } finally {
            em.close();
        }
    }

    public List<Customer> getAllCustomers() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Customer> num = em.createQuery("Select c from Customer c", Customer.class);
            return num.getResultList();
        } finally {
            em.close();
        }
    }
    
    public ItemType addItemType(String name, String description, double price) {
        ItemType c = new ItemType(name, description, price);
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
            return c;
        } finally {
            em.close();
        }
    }

    public ItemType findItemType(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            ItemType c = em.find(ItemType.class, id);
            return c;
        } finally {
            em.close();
        }
    }

    public List<ItemType> getAllItemTypes() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ItemType> num = em.createQuery("Select c from ItemType c", ItemType.class);
            return num.getResultList();
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.DROP_AND_CREATE);
        CustomerFacade facade = CustomerFacade.getCustomerFacade(emf);
        EntityManager em = emf.createEntityManager();

        // Adding customers
        System.out.println("\nAdding customers");
        facade.addCustomer("Hans Jørgensen", "hans@email.com");
        facade.addCustomer("Marianne Jørgensen", "marianne@email.com");
        facade.addCustomer("Jens Hansen", "jens@email.com");
        facade.addCustomer("Trine Jensen", "trine@email.com");

        // Finding a customer
        System.out.println("\nFinding customer 2:\n"
                + facade.findCustomer(2));
        
        //Finding all customers
        System.out.println("\nGetting all customers:");
        for (Customer allCustomer : facade.getAllCustomers()) {
            System.out.println(allCustomer);
        }

        // Adding itemtypes
        System.out.println("\nAdding itemtypes");
        facade.addItemType("Blå maling", "Det er blåt og flydende", 500.50);
        facade.addItemType("Rød maling", "Det er rødt og flydende", 510.50);
        facade.addItemType("Gul maling", "Det er gult og flydende", 400.50);
        facade.addItemType("Grøn maling", "Det er grønt og der er klumper i", 475.50);
        
        
        
    }

}
