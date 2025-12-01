package vn.clothing.fashion_shop.web.rest.DTO.responses;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.web.rest.DTO.requests.OrderRequest.InnerOrderRequest;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ProductRequest.InnerProductRequest;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ProductSkuRequest.InnerProductSkuRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.OrderResponse.InnerOrderResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ProductResponse.InnerProductResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ProductSkuResponse.InnerProductSkuResponse;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {
    private Long id;
    private Double discountPrice;
    private Double price;
    private Double priceOriginal;
    private Integer quantity;
    private Double totalPrice;

    private InnerOrderResponse order;
    private InnerProductResponse product;
    private InnerProductSkuResponse productSku;

    private boolean activated;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class InnerOrderDetailResponse {
        private Long id;
        private Double discountPrice;
        private Double price;
        private Double priceOriginal;
        private Integer quantity;
        private Double totalPrice;
    }
}
