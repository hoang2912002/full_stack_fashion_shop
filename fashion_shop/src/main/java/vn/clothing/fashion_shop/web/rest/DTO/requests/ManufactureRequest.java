package vn.clothing.fashion_shop.web.rest.DTO.requests;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.domain.Category;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManufactureRequest {
    private Long id;
    private String name;
    private String slug;
    private String logo;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private boolean activated;
    @JsonProperty("isCreate")
    private boolean isCreate;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InnerManufactureRequest {
        private Long id;
    }
}
