package vn.clothing.fashion_shop.web.rest.DTO.product;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.clothing.fashion_shop.domain.Manufacture;
import vn.clothing.fashion_shop.domain.ProductSku;
import vn.clothing.fashion_shop.web.rest.DTO.category.GetCategoryDTO.InnerGetCategoryDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetProductDTO {
    private Long id;
    private String name;
    private String price;
    private String thumbnail;
    private int quantity;
    
    private Manufacture manufacture;
    private InnerGetCategoryDTO category;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    private List<ProductSku> productSkus;
}
