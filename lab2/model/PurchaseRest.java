package lab2.model;

import lombok.Data;
import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@Table(name = "purchase")
public class PurchaseRest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date date;

    @ManyToOne (optional=false)
    private Shop shop;

    @ManyToOne (optional=false)
    private Customer customer;

    @ManyToOne (optional=false)
    private Book book;

    private int quantity;
    private double price;
}
