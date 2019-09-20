package facades;

import entities.Customer;
import entities.ItemType;
import entities.OrderLine;
import entities.Ordrer;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import utils.EMF_Creator;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class TheFacade {

    private static TheFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private TheFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static TheFacade getTheFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TheFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * CUSTOMERS
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

    /**
     * ITEMTYPES
     */
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

    /**
     * ORD(R)ERS
     */
    public Ordrer findOrdrer(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            Ordrer c = em.find(Ordrer.class, id);
            return c;
        } finally {
            em.close();
        }
    }

    /**
     * ORD(R)ERS
     */
    public Ordrer addOrdrerToCustomer(Customer c, Ordrer o) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            c.addOrder(o);
            em.persist(c);
            em.getTransaction().commit();
            return o;
        } finally {
            em.close();
        }
    }

    /**
     * Find all Orders, for a specific Customer
     */
    public List<Ordrer> getAllOrdersPerCustomer(int customerID) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Ordrer> num
                    = em.createQuery("SELECT o FROM Ordrer o JOIN o.customer c where c.customerID = :customerID",
                            Ordrer.class)
                            .setParameter("customerID", customerID);
            return num.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Find the total price of an order
     */
    public List<OrderLine> getTotalOrderPrice(int orderID) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<OrderLine> num
                    = em.createQuery("SELECT ol FROM OrderLine ol where ol.ordrer.ordrerID = :orderID",
                            OrderLine.class)
                            .setParameter("orderID", orderID);
            if (num.getResultList() != null) {
                return num.getResultList();
            }
            return null;
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.DROP_AND_CREATE);
        TheFacade facade = TheFacade.getTheFacade(emf);

        // Adding customers
        System.out.println("\nAdding customers");
        facade.addCustomer("Hans Jørgensen", "hans@email.com");
        facade.addCustomer("Marianne Jørgensen", "marianne@email.com");
        facade.addCustomer("Jens Hansen", "jens@email.com");
        facade.addCustomer("Trine Jensen", "trine@email.com");

        // Finding a customer
        System.out.println("\nFinding customer #2:\n"
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

        // Finding an itemtype
        System.out.println("\nFinding itemtype #3:\n"
                + facade.findItemType(3));

        // Create an Order and Add it to a Customer
        System.out.println("\nCreate an Order");
        Customer cust = facade.findCustomer(1);
        System.out.println("Add order to customer: #" + cust.getCustomerID() + " "
                + cust.getName());
        Ordrer o1 = new Ordrer();
        cust.addOrder(o1);
        facade.addOrdrerToCustomer(cust, o1);     //trying to avoid making more customers in DB (not working)
//        persisting(cust);                           // helper function containing the persist() part

        //Create an OrderLine for a specific ItemType, and add it to an Order
        System.out.println("\nCreating an OrderLine");
        OrderLine ol = new OrderLine(4);
        ol.setItemType(facade.findItemType(1));
        Ordrer o = facade.findOrdrer(1);            // Ordrer using #1 to save time
        o.addOrderLine(ol);

        persisting(o);

//      Find all Orders, for a specific Customer
        List<Ordrer> oList = facade.getAllOrdersPerCustomer(5);
        System.out.println("\nAll orders from customer #5:");
        double totalPrice = 0;
        for (Ordrer or : oList) {
            System.out.println("Order #" + or.getOrdrerID() + " - " + or.getCustomer());

//      Find the total price of an order   
            List<OrderLine> ol1List = facade.getTotalOrderPrice(or.getOrdrerID());
            for (OrderLine orderLine : ol1List) {
                int quantity = orderLine.getQuantity();
                double price = orderLine.getItemType().getPrice();
                totalPrice += quantity * price;
                System.out.println("Orderline price: " + quantity
                        + " x " + price + " kr."
                        + " = " + quantity * price + " kr.");
            }

        }
        System.out.println("Order total price: " + totalPrice + " kr.");

    }

    private static void persisting(Object cust) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cust);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

}
