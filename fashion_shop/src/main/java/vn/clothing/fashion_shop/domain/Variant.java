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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Variant extends AbstractAuditingEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public Long getId() {
        return this.id;
    }

    private boolean activated;
    // private Instant createdAt;
    // private Instant updatedAt;
    // private String createdBy;
    // private String updatedBy;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;
    
    @ManyToOne()
    @JoinColumn(name = "sku_id")
    @JsonBackReference
    private ProductSku sku;

    @ManyToOne()
    @JoinColumn(name = "option_id")
    @JsonManagedReference
    private Option option;

    @ManyToOne()
    @JoinColumn(name = "option_value_id")
    @JsonManagedReference
    private OptionValue optionValue;

}
