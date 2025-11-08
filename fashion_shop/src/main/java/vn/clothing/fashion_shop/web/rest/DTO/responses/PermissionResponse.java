package vn.clothing.fashion_shop.web.rest.DTO.responses;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@Data
@AllArgsConstructor
public class PermissionResponse {
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

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class InnerPermissionResponse {
        private Long id;
        private String name;
        private String apiPath;
        private String method;
        private String module;
    }
}
