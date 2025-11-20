package vn.clothing.fashion_shop.web.rest.DTO.requests;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ProductRequest.InnerProductRequest;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSkuRequest {
    private Long id;
    private String sku;
    private Double price;
    private Integer tempStock;
    private String thumbnail;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private boolean activated;
    private InnerProductRequest product;

    @JsonProperty("isCreate")
    private boolean isCreate;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InnerProductSkuRequest {
        private Long id;
    }
}
