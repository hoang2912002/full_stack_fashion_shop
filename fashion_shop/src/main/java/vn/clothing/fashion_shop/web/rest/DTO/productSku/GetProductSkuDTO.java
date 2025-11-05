package vn.clothing.fashion_shop.web.rest.DTO.productSku;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetProductSkuDTO {
    private Long id;
    private String sku;
    private Double price;
    private Integer stock;
    private String thumbnail;
    private boolean activated;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InnerProductSkuDTO{
        private Long id;
        private String sku;
        private Double price;
        private Integer stock;
        private String thumbnail;
    }
}
