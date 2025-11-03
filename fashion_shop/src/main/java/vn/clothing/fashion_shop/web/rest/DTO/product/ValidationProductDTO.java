package vn.clothing.fashion_shop.web.rest.DTO.product;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.clothing.fashion_shop.domain.Category;
import vn.clothing.fashion_shop.domain.Manufacture;
import vn.clothing.fashion_shop.domain.OptionValue;
import vn.clothing.fashion_shop.web.validation.product.ProductMatching;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ProductMatching
public class ValidationProductDTO {
    private Long id;
    private String name;
    private Double price;
    private String thumbnail;
    private int quantity;
    
    private String description;
    private Manufacture manufacture;
    private Category category;
    @JsonProperty("isCreate")
    private boolean isCreate;

    private List<InnerVariant> variants;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class InnerVariant {
        private String skuId;
        private List<OptionValue> optionValues;
        private Double price;
        private int stock;
        private String thumbnail;
    }
}
