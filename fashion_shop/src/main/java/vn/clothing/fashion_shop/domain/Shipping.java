package vn.clothing.fashion_shop.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.constants.enumEntity.ShippingEnum;
@Entity
@Table(name = "shippings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipping extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public Long getId() {
        return this.id;
    }

    private String provider;      // GHN, GHTK, VNPost…
    private String trackingCode;  // mã vận đơn
    private BigDecimal shippingFee;// phí ship

    private LocalDateTime estimatedDate;// dự kiến giao
    private LocalDateTime shippedAt;// thời gian gửi
    private LocalDateTime deliveredAt;// thời gian giao
    private boolean activated;

    @Enumerated(EnumType.STRING)
    private ShippingEnum status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true)
    @JsonBackReference
    private Order order;
}
