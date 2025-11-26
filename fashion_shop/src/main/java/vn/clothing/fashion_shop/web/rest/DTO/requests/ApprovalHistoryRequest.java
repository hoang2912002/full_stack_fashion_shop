package vn.clothing.fashion_shop.web.rest.DTO.requests;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ApprovalMasterRequest.InnerApprovalMasterRequest;
import vn.clothing.fashion_shop.web.validation.approval_history.ApprovalHistoryMatching;

@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
@ApprovalHistoryMatching
public class ApprovalHistoryRequest {
    private Long id;
    private Instant approvedAt;
    private Integer requestId; 
    private String note;
    private InnerApprovalMasterRequest approvalMaster;
    private boolean activated;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    @JsonProperty("isCreate")
    private boolean isCreate;
    private String entityType;

    @Builder
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class InnerApprovalHistoryRequest {
        private Long id;
    }
}
