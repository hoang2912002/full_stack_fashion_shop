package vn.clothing.fashion_shop.web.rest.DTO.responses;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.constants.enumEntity.PromotionEnum;
import vn.clothing.fashion_shop.web.rest.DTO.responses.CategoryResponse.InnerCategoryResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ProductResponse.InnerProductResponse;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionResponse {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Double discountPercent;
    private Double minDiscountAmount;
    private Double maxDiscountAmount;
    private Integer quantity;
    private PromotionEnum discountType;
    private LocalDate startDate;
    private LocalDate endDate;
    private byte optionPromotion;

    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private boolean activated;

    private List<InnerProductResponse> products;
    private List<InnerCategoryResponse> categories;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InnerPromotionResponse {
        private Long id;
        private String code;
        private String name;
        private Double discountPercent;
        private Double minDiscountAmount;
        private Double maxDiscountAmount;
        private Integer quantity;
        private PromotionEnum discountType;
    }
}
