package vn.clothing.fashion_shop.web.rest.DTO.responses;

import java.time.Instant;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.constants.enumEntity.CouponEnum;
import vn.clothing.fashion_shop.web.validation.category.CategoryMatching;

@CategoryMatching
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse {
    private Long id;
    private String name;
    private String code;
    private Double couponAmount;
    private Instant startDate;
    private Instant endDate;
    private Integer stock;
    @Enumerated(EnumType.STRING)
    private CouponEnum type;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private boolean activated;

    @CategoryMatching
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InnerCouponResponse {
        private Long id;
        private String name;
        private String code;
        private Double couponAmount;
    }
}
