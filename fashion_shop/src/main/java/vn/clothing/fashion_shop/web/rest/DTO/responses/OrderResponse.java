package vn.clothing.fashion_shop.web.rest.DTO.responses;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.constants.enumEntity.OrderEnum;
import vn.clothing.fashion_shop.web.rest.DTO.responses.CouponResponse.InnerCouponResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.OrderDetailResponse.InnerOrderDetailResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.UserResponse.InnerUserResponse;
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class OrderResponse {
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

    private InnerCouponResponse coupon;
    private InnerUserResponse user;
    private List<InnerOrderDetailResponse> orderDetails;

    private boolean activated;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class InnerOrderResponse {
        private Long id;
        @Enumerated(EnumType.STRING)
        private OrderEnum status; 
        private Integer totalItem;
        private Double totalPrice;  
        private Double finalPrice;
        private String paymentMethod;
    }
}
