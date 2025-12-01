package vn.clothing.fashion_shop.web.rest.DTO.requests;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.web.rest.DTO.requests.OrderRequest.InnerOrderRequest;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ProductRequest.InnerProductRequest;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ProductSkuRequest.InnerProductSkuRequest;
import vn.clothing.fashion_shop.web.validation.approval_master.ApprovalMasterMatching;
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class OrderDetailRequest {
    private Long id;
    private Double discountPrice;
    private Double price;
    private Double priceOriginal;
    private Integer quantity;
    private Double totalPrice;

    private InnerOrderRequest order;
    private InnerProductRequest product;
    private InnerProductSkuRequest productSku;

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
    public static class InnerOrderDetailRequest {
        private Long id;        
    }
}
