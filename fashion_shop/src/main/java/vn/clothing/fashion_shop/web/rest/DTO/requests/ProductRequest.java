package vn.clothing.fashion_shop.web.rest.DTO.requests;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.domain.Category;
import vn.clothing.fashion_shop.domain.Manufacture;
import vn.clothing.fashion_shop.web.rest.DTO.requests.CategoryRequest.InnerCategoryRequest;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ManufactureRequest.InnerManufactureRequest;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ShopManagementRequest.InnerShopManagementRequest;
import vn.clothing.fashion_shop.web.rest.DTO.requests.VariantRequest.InnerVariantRequest;
import vn.clothing.fashion_shop.web.validation.product.ProductMatching;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ProductMatching
public class ProductRequest {
    private Long id;
    private String name;
    private Double price;
    private String thumbnail;
    // private Integer quantity;
    
    private String description;
    private InnerManufactureRequest manufacture;
    private InnerCategoryRequest category;
    private InnerShopManagementRequest shopManagement;
    @JsonProperty("isCreate")
    private boolean isCreate;

    private List<InnerVariantRequest> variants;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InnerProductRequest {
        private Long id;
    }
}
