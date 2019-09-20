package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQuery(name = "Ordrer.deleteAllRows", query = "DELETE from Ordrer")
public class Ordrer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ordrerID;

    @OneToMany(mappedBy = "ordrer")
    private List<OrderLine> orderLines = new ArrayList();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "customerID")
    private Customer customer;

    public Ordrer() {
    }

    public Integer getOrdrerID() {
        return ordrerID;
    }

    public void setOrderID(Integer orderID) {
        this.ordrerID = orderID;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void addOrderLine(OrderLine ol) {
        this.orderLines.add(ol);
        if (ol.getOrdrer() != this) {
            ol.setOrdrer(this);
        }
    }

    @Override
    public String toString() {
        return "Ordrer{" + "ordrerID=" + ordrerID + ", orderLines=" + orderLines + ", customer=" + customer + '}';
    }

}
