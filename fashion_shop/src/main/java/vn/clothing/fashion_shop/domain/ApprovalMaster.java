package vn.clothing.fashion_shop.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.constants.enumEntity.ApprovalMasterEnum;

@Entity
@Table(name = "approval_masters", indexes = {
    @Index(name = "idx_approval_entity_step", columnList = "entityType, step"),
    @Index(name = "idx_approval_status", columnList = "status")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalMaster extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public Long getId() {
        return this.id;
    }
    private String entityType; // -- PRODUCT, INVENTORY, PURCHASE_ORDER...

    private Integer step; // 1, 2, 3, 4

    @Enumerated(EnumType.STRING)
    private ApprovalMasterEnum status;

    private Boolean required = Boolean.FALSE; // -- Bước này có bắt buộc hay không

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @JsonBackReference
    private User user;

    @OneToMany( 
        mappedBy = "approvalMaster", 
        fetch = FetchType.LAZY,
        cascade = CascadeType.PERSIST
    )
    @JsonManagedReference
    List<ApprovalHistory> approvalHistories = new ArrayList<>();
}
