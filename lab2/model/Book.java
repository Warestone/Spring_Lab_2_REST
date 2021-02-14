package lab2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private double cost;
    private String storage;
    private int quantity;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book", orphanRemoval = true, fetch=FetchType.LAZY)
    private List<PurchaseRest> purchases;
}
