package vn.clothing.fashion_shop.web.rest.DTO.requests;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.constants.enumEntity.ApprovalMasterEnum;
import vn.clothing.fashion_shop.web.rest.DTO.requests.RoleRequest.InnerRoleRequest;
import vn.clothing.fashion_shop.web.rest.DTO.requests.UserRequest.InnerUserRequest;
import vn.clothing.fashion_shop.web.validation.approval_master.ApprovalMasterMatching;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@ApprovalMasterMatching
public class ApprovalMasterRequest {
    private Long id;
    private String entityType; // -- PRODUCT, INVENTORY, PURCHASE_ORDER...
    private Integer step; // 1, 2, 3, 4
    @Enumerated(EnumType.STRING)
    private ApprovalMasterEnum status;
    private Boolean required;
    private InnerRoleRequest role;
    private InnerUserRequest user;
    private boolean activated;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    @JsonProperty("isCreate")
    private boolean isCreate;

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class InnerApprovalMasterRequest {
        private Long id;
    }
}
