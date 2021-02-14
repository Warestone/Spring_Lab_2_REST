package lab2.model;

import lombok.Data;
import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@Table(name = "purchase")
public class PurchaseDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date date;

    @Column(name = "shop_id")
    private int shopId;

    @Column(name = "customer_id")
    private int customerId;

    @Column(name = "book_id")
    private int bookId;

    private int quantity;

    private double price;
}
