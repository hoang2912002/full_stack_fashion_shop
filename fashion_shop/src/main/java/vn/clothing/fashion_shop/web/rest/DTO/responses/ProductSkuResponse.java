package vn.clothing.fashion_shop.web.rest.DTO.responses;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.web.rest.DTO.responses.CategoryResponse.InnerCategoryResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ManufactureResponse.InnerManufactureResponse;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSkuResponse {
    private Long id;
    private String sku;
    private Double price;
    private String thumbnail;
    private int stock;
    
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private boolean activated;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InnerProductSkuResponse {
        private Long id;
        private String sku;
        private Double price;
        private String thumbnail;
        private int stock;
    }
}
