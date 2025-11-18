package vn.clothing.fashion_shop.domain;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory extends AbstractAuditingEntity {
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


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_sku_id", unique = true)
    @JsonBackReference
    private ProductSku productSku;

    private Integer quantityAvailable; // tồn kho hiện tại
    private Integer quantityReserved; // số lượng đã giữ (nếu dùng pre-reserve) Đây là số lượng hàng tạm khóa để giữ cho đơn hàng chưa thanh toán hoặc chưa giao.     
    private Integer quantitySold; // tổng đã bán
}
