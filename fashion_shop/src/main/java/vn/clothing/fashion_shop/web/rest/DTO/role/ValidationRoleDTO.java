package vn.clothing.fashion_shop.web.rest.DTO.role;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import vn.clothing.fashion_shop.domain.Permission;
import vn.clothing.fashion_shop.web.validation.role.RoleMatching;

@Setter
@Getter
@RoleMatching
public class ValidationRoleDTO {
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

    List<Permission> permissions;

    @Getter
    @Setter
    public static class InnerValidationRoleDTO {
        private Long id;
    }
}
