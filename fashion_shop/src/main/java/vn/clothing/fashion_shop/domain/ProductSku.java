package vn.clothing.fashion_shop.domain;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_skus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private Integer stock;

    private boolean activated;
    // private Instant createdAt;
    // private Instant updatedAt;
    // private String createdBy;
    // private String updatedBy;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany( mappedBy = "sku", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    List<Variant> variants;
}
