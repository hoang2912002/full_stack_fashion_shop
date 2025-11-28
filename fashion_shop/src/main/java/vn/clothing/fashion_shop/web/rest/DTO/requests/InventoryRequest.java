package vn.clothing.fashion_shop.web.rest.DTO.requests;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ProductRequest.InnerProductRequest;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ProductSkuRequest.InnerProductSkuRequest;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
    private Long id;
    private Integer quantityAvailable;
    private Integer quantityReserved;
    private Integer quantitySold;
    private InnerProductRequest product;
    private InnerProductSkuRequest productSku;
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
    public static class InnerInventoryRequest {
        private Long id;
        private Integer quantity;
        private Integer quantityReserved;
        private Integer quantitySold;
        private String referenceType;
        private Long referenceId;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaseInventoryRequest {
        private Long id;
        private ProductSkuRequest sku;
        private Integer quantity;
        private String referenceType;
        private Long referenceId;
        private String note;
    }
}
