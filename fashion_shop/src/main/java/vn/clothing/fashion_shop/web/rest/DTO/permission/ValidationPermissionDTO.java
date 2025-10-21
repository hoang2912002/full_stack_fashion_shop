package vn.clothing.fashion_shop.web.rest.DTO.permission;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.clothing.fashion_shop.domain.Permission;
import vn.clothing.fashion_shop.web.validation.permission.PermissionMatching;

@Setter
@Getter
@PermissionMatching
public class ValidationPermissionDTO{
    private Long id;
    private String apiPath;
    private String method;
    private String module;
    private String name;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updateAt;
    private boolean activated;
    @JsonProperty("isCreate")
    private boolean isCreate;
}
