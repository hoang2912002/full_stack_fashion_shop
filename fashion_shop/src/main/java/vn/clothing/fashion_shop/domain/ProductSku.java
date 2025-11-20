package vn.clothing.fashion_shop.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_skus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSku extends AbstractAuditingEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public Long getId() {
        return this.id;
    }

    private String sku;
    private Double price;
    private Integer tempStock; //Lưu tạm số lượng trong kho chờ duyệt

    private boolean activated;
    private String thumbnail;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    @OneToMany( 
        mappedBy = "sku",  
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
        // cascade = CascadeType.PERSIST
    )
    @JsonManagedReference
    List<Variant> variants;

    @OneToMany(
        mappedBy = "productSku", 
        fetch = FetchType.LAZY
        // cascade = CascadeType.ALL,
        // orphanRemoval = true
    )
    @JsonBackReference
    List<OrderDetail> orderDetails = new ArrayList<>();


    @OneToOne(
        mappedBy = "productSku", 
        fetch = FetchType.LAZY 
        // cascade = CascadeType.ALL, 
        // optional = true
    )
    @JsonManagedReference
    private Inventory inventory;

    @OneToMany( 
        mappedBy = "productSku", 
        fetch = FetchType.LAZY
        // cascade = CascadeType.ALL,
        // orphanRemoval = true
    )
    @JsonManagedReference
    List<InventoryTransaction> inventoryTransactions = new ArrayList<>();
}
