package vn.clothing.fashion_shop.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public Long getId() {
        return this.id;
    }
    
    private String name;
    private String slug;

    private Double price;
    private String thumbnail;
    // private Integer quantity;
    
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private boolean activated;

    @ManyToOne()
    @JoinColumn(name = "manufacture_id")
    private Manufacture manufacture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany( 
        mappedBy = "product", 
        fetch = FetchType.LAZY
    )
    @JsonManagedReference
    List<Inventory> inventories = new ArrayList<>();

    @OneToMany( 
        mappedBy = "product", 
        fetch = FetchType.LAZY,
        cascade = CascadeType.PERSIST
    )
    @JsonManagedReference
    List<ProductSku> productSkus = new ArrayList<>();

    @OneToMany( 
        mappedBy = "product", 
        fetch = FetchType.LAZY,
        // cascade = CascadeType.ALL,
        // orphanRemoval = true
        cascade = CascadeType.PERSIST
    )
    @JsonManagedReference
    List<Variant> variants = new ArrayList<>();

    @OneToMany(
        mappedBy = "product", 
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonManagedReference
    List<PromotionProduct> promotionProducts = new ArrayList<>();

    @OneToMany(
        mappedBy = "product", 
        fetch = FetchType.LAZY
        // cascade = CascadeType.ALL,
        // orphanRemoval = true
    )
    @JsonBackReference
    List<OrderDetail> orderDetails = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_management_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ShopManagement shopManagement;
}
