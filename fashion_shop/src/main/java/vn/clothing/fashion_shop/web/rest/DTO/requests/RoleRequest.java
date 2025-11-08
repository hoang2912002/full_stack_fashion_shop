package vn.clothing.fashion_shop.web.rest.DTO.requests;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.web.rest.DTO.requests.PermissionRequest.InnerPermissionRequest;
import vn.clothing.fashion_shop.web.validation.role.RoleMatching;
@NoArgsConstructor
@Data
@AllArgsConstructor
@RoleMatching
public class RoleRequest {
    private Long id;
    private String name;
    private String slug;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updateAt;
    private boolean activated;

    @JsonProperty("isCreate")
    private boolean isCreate;

    List<InnerPermissionRequest> permissions;

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class InnerRoleRequest {
        private Long id;
    }
}
