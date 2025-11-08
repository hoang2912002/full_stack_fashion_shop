package vn.clothing.fashion_shop.web.rest.DTO.requests;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@Data
@AllArgsConstructor
public class PermissionRequest {
    private Long id;
    private String name;
    private String apiPath;
    private String method;
    private String module;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updateAt;
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
