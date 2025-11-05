package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.ProductSku;
import vn.clothing.fashion_shop.web.rest.DTO.productSku.GetProductSkuDTO;

@Mapper(componentModel = "spring")
public interface ProductSkuMapper extends EntityMapper<GetProductSkuDTO, ProductSku> {
    ProductSkuMapper INSTANCE = Mappers.getMapper(ProductSkuMapper.class);

    @Named("toDto")
    GetProductSkuDTO toDto(ProductSku productSku);
    List<GetProductSkuDTO> toDto(List<ProductSku> productSkus);

    @Named("toMiniDto")
    GetProductSkuDTO toMiniDto(ProductSku productSku);
    // Map từ DTO sang Entity
    ProductSku toEntity(GetProductSkuDTO dto);
}
