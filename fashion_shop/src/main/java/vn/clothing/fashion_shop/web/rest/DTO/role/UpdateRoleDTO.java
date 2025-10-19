package vn.clothing.fashion_shop.web.rest.DTO.role;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateRoleDTO {
    private Long id;
    private String name;
    private String slug;
    private boolean activated;
    private String updatedBy;
    private Instant updatedAt;
    private List<InnerPermissionRoleDTO> permissions;

    @Setter
    @Getter
    public static class InnerPermissionRoleDTO {
        private Long id;
        private String name;
    }
}
