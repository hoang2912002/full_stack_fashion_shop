package vn.clothing.fashion_shop.web.rest.DTO.responses;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PermissionResponse.InnerPermissionResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleResponse {
    private Long id;
    private String name;
    private String slug;
    private boolean activated;
    private String createdBy;
    private String updatedBy;
    private Instant createdAt;
    private Instant updatedAt;
    private List<InnerPermissionResponse> permissions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class InnerRoleResponse {
        private Long id;
        private String name;
        private String slug;
    }
}
