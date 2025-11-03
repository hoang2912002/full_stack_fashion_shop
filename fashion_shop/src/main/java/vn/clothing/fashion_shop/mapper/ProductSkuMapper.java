package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.ProductSku;
import vn.clothing.fashion_shop.web.rest.DTO.productSku.GetProductSkuDTO;

public interface ProductSkuMapper extends EntityMapper<GetProductSkuDTO, ProductSku> {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    GetProductSkuDTO toDto(ProductSku entity);
    List<GetProductSkuDTO> toDto(List<ProductSku> products);

    // Map từ DTO sang Entity
    ProductSku toEntity(GetProductSkuDTO dto);
}
