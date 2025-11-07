package vn.clothing.fashion_shop.web.rest.DTO.responses;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    // private List<InnerPermissionRoleDTO> permissions;

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
