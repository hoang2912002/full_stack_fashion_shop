package vn.clothing.fashion_shop.web.rest.DTO.responses;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ProductResponse.InnerProductResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ProductSkuResponse.InnerProductSkuResponse;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    private Long id;
    private Integer quantityAvailable;
    private Integer quantityReserved;
    private Integer quantitySold;
    private InnerProductResponse product;
    private InnerProductSkuResponse productSku;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private boolean activated;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InnerInventoryResponse {
        private Long id;
        private Integer quantityAvailable;
        private Integer quantityReserved;
        private Integer quantitySold;        
    }
}
