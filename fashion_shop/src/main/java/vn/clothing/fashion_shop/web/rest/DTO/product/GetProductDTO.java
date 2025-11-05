package vn.clothing.fashion_shop.web.rest.DTO.product;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.clothing.fashion_shop.domain.Manufacture;
import vn.clothing.fashion_shop.web.rest.DTO.category.GetCategoryDTO.InnerGetCategoryDTO;
import vn.clothing.fashion_shop.web.rest.DTO.option.GetOptionDTO;
import vn.clothing.fashion_shop.web.rest.DTO.option.GetOptionDTO.InnerOptionDTO;
import vn.clothing.fashion_shop.web.rest.DTO.productSku.GetProductSkuDTO.InnerProductSkuDTO;

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

    private List<InnerProductSkuDTO> productSkus;

    private List<InnerOptionDTO> options;
    private List<GetOptionDTO.InnerOptionValueDTO> optionValues;
}
