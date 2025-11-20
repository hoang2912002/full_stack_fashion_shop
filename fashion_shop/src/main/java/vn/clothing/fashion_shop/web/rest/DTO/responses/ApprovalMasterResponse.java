package vn.clothing.fashion_shop.web.rest.DTO.responses;

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

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class ApprovalMasterResponse {
    private Long id;
    private String entityType;
    private Integer step;
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
    

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class InnerApprovalMasterResponse {
        private Long id;
    }
}
