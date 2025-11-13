package vn.clothing.fashion_shop.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "promotion_products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionProduct extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public Long getId() {
        return this.id;
    }

    private boolean activated;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;
    
    @ManyToOne()
    @JoinColumn(name = "promotion_id")
    @JsonBackReference
    private Promotion promotion;
    
    @ManyToOne()
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;
}
