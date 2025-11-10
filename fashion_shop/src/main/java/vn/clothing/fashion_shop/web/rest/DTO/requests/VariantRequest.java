package vn.clothing.fashion_shop.web.rest.DTO.requests;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.domain.Option;
import vn.clothing.fashion_shop.domain.OptionValue;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ProductRequest.InnerProductRequest;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ProductSkuRequest.InnerProductSkuRequest;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VariantRequest {
    private Long id;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private boolean activated;
    private Option option;
    private OptionValue optionValue;
    private InnerProductRequest product;
    private InnerProductSkuRequest productSku;
    @JsonProperty("isCreate")
    private boolean isCreate;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InnerVariantRequest {
        private String skuId;
        private List<String> optionValues;
        private Double price;
        private int stock;
        private String thumbnail;
    }
}
