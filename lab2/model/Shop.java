package lab2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "shop")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String region;
    private double commission_pct;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shop", orphanRemoval = true, fetch=FetchType.LAZY)
    private List<PurchaseRest> purchases;
}
