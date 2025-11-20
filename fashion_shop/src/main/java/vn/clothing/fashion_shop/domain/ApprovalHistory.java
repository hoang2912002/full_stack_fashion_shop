package vn.clothing.fashion_shop.domain;

import java.time.Instant;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Index;
@Entity
@Table(name = "approval_histories", indexes = {
    @Index(name = "idx_approval_request_id", columnList = "requestId"),
    @Index(name = "idx_approval_master_id", columnList = "approval_master_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalHistory extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public Long getId() {
        return this.id;
    }

    private Long requestId; // -- ID của entity (product_id, inventory_id...)

    @Column(columnDefinition = "TEXT")
    private String note;

    private Instant approvedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_master_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ApprovalMaster approvalMaster;
}
