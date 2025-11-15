package vn.clothing.fashion_shop.web.rest.DTO.responses;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ProductResponse.InnerProductResponse;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private String slug;
    private InnerCategoryResponse parent;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private boolean activated;
    // private InnerProductResponse products;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InnerCategoryResponse {
        private Long id;
        private String name;
        private String slug;
    }


    public static class InnerCategoryTreeResponse {
        
    }
}
