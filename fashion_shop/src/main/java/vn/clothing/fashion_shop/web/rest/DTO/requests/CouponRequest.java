package vn.clothing.fashion_shop.web.rest.DTO.requests;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.constants.enumEntity.CouponEnum;
import vn.clothing.fashion_shop.web.validation.coupon.CouponMatching;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CouponMatching
public class CouponRequest {
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
    @JsonProperty("isCreate")
    private boolean isCreate;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InnerCouponRequest {
        private Long id;
    }
}
