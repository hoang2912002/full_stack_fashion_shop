package vn.clothing.fashion_shop.web.rest.DTO.requests;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.web.validation.permission.PermissionMatching;
@NoArgsConstructor
@Data
@AllArgsConstructor
@PermissionMatching
public class PermissionRequest {
    private Long id;
    private String name;
    private String apiPath;
    private String method;
    private String module;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private boolean activated;

    @JsonProperty("isCreate")
    private boolean isCreate;
    
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class InnerPermissionRequest {
        private Long id;
    }
}
