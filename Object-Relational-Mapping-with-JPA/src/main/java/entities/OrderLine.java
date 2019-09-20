package entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;


@Entity
@NamedQuery(name = "OrderLine.deleteAllRows", query = "DELETE from OrderLine")
public class OrderLine implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderLineID;
    private Integer quantity;
    
    private Ordrer ordrer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemTypeID")
    private ItemType itemType;
    
    public OrderLine() {
    }

    public OrderLine(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getOrderLineID() {
        return orderLineID;
    }

    public void setOrderLineID(Integer orderLineID) {
        this.orderLineID = orderLineID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Ordrer getOrdrer() {
        return ordrer;
    }

    public void setOrdrer(Ordrer ordrer) {
        this.ordrer = ordrer;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public String toString() {
        return "OrderLine{" + "orderLineID=" + orderLineID + ", quantity=" + quantity + ", ordrer=" + ordrer + ", itemType=" + itemType + '}';
    }

  
   
}
