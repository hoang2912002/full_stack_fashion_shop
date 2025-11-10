package vn.clothing.fashion_shop.web.rest.DTO.responses;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManufactureResponse {
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
    public static class InnerManufactureResponse {
        private Long id;
        private String name;
        private String slug;
    }
}
