package vn.clothing.fashion_shop.web.rest.DTO.requests;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.constants.enumEntity.PromotionEnum;
import vn.clothing.fashion_shop.web.rest.DTO.requests.CategoryRequest.InnerCategoryRequest;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ProductRequest.InnerProductRequest;
import vn.clothing.fashion_shop.web.validation.promotion.PromotionMatching;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@PromotionMatching
public class PromotionRequest {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Double discountPercent;
    private Double minDiscountAmount;
    private Double maxDiscountAmount;
    private Integer quantity;
    private PromotionEnum discountType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime endDate;
    private byte optionPromotion;

    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private boolean activated;

    @JsonProperty("isCreate")
    private boolean isCreate;

    private List<InnerProductRequest> products;
    private List<InnerCategoryRequest> categories;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InnerPromotionRequest {
        private Long id;
        private String code;
    }
}
