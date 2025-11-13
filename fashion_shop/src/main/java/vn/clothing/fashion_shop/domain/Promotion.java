package vn.clothing.fashion_shop.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.clothing.fashion_shop.constants.enumEntity.PromotionEnum;
@Entity
@Table(name = "promotions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promotion extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public Long getId() {
        return this.id;
    }
    private String code;
    private String name;
    private String description;

    private Double discountPercent;
    private Double minDiscountAmount;
    private Double maxDiscountAmount;
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private PromotionEnum discountType;

    private LocalDate startDate;
    private LocalDate endDate;

    private boolean activated;
    private byte optionPromotion;

    @OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<PromotionProduct> promotionProducts = new ArrayList<>();
}
