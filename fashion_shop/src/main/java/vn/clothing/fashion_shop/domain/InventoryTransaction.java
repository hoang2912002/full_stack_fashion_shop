package vn.clothing.fashion_shop.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
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
import vn.clothing.fashion_shop.constants.enumEntity.InventoryTransactionTypeEnum;

@Entity
@Table(name = "inventory_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryTransaction extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public Long getId() {
        return this.id;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "product_sku_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @JsonBackReference
    private ProductSku productSku;

    private boolean activated;

    @Enumerated(EnumType.STRING)
    private InventoryTransactionTypeEnum type; // Hành động tồn kho

    private Integer quantityChange; // số lượng thay đổi trong giao dịch

    private Integer beforeQuantity; // số lượng tồn kho trước khi thực hiện giao dịch.
    private Integer afterQuantity; // Số lượng tồn kho sau khi thực hiện giao dịch. => beforeQuantity + quantityChange

    /*
     * ORDER,
     * CANCEL,
     * PURCHASE_ORDER: nhập hàng ncc,
     * SYSTEM: hệ thống tự update,
     * MANUAL: admin tự chỉnh
     * => Nguồn gốc nghiệp vụ
     */
    private String referenceType; 

    /*
     * ORDER_RESERVE → referenceId = orderId
     * PURCHASE_ORDER → referenceId = purchaseOrderId
     * RETURN → referenceId = returnRequestId
     */
    private Long referenceId;

    private String note;
}
