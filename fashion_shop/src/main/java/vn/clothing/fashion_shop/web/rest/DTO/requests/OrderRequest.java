package vn.clothing.fashion_shop.web.rest.DTO.requests;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.constants.enumEntity.OrderEnum;
import vn.clothing.fashion_shop.web.rest.DTO.requests.CouponRequest.InnerCouponRequest;
import vn.clothing.fashion_shop.web.rest.DTO.requests.UserRequest.InnerUserRequest;
import vn.clothing.fashion_shop.web.validation.approval_master.ApprovalMasterMatching;
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class OrderRequest {
    private Long id;
    private Double discountPrice;    // tổng giảm giá
    private Double finalPrice;       // giá phải trả
    private String note;
    private String paymentMethod; // COD, MOMO, BANK
    
    private String receiverName;
    private String receiverEmail;
    private String receiverPhone;
    private String receiverAddress;
    private String receiverCity;
    private String receiverDistrict;
    private String receiverWard;

    @Enumerated(EnumType.STRING)
    private OrderEnum status; 
    private Integer totalItem;
    private Double totalPrice;       // giá tạm tính

    private InnerCouponRequest coupon;
    private InnerUserRequest user;

    private boolean activated;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    @JsonProperty("isCreate")
    private boolean isCreate;

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    @ApprovalMasterMatching
    public static class InnerOrderRequest {
        private Long id;
    }
}
