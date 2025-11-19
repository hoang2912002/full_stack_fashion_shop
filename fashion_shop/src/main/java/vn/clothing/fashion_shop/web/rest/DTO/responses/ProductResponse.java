package vn.clothing.fashion_shop.web.rest.DTO.responses;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.web.rest.DTO.responses.CategoryResponse.InnerCategoryResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ManufactureResponse.InnerManufactureResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.OptionResponse.InnerOptionResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.OptionValueResponse.InnerOptionValueResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ProductSkuResponse.InnerProductSkuResponse;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String price;
    private String thumbnail;
    private Integer quantity;
    
    private String description;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private boolean activated;

    private InnerManufactureResponse manufacture;
    private InnerCategoryResponse category;
    private List<InnerProductSkuResponse> productSkus;
    private List<InnerOptionResponse> options;
    private List<InnerOptionValueResponse> optionValues;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InnerProductResponse {
        private Long id;
        private String name;
    }
 }
