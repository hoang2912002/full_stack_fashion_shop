package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.ProductSku;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ProductSkuResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ProductSkuResponse.InnerProductSkuResponse;

@Mapper(componentModel = "spring")
public interface ProductSkuMapper extends EntityMapper<ProductSkuResponse, ProductSku> {
    ProductSkuMapper INSTANCE = Mappers.getMapper(ProductSkuMapper.class);

    @Named("toDto")
    ProductSkuResponse toDto(ProductSku productSku);
    List<ProductSkuResponse> toDto(List<ProductSku> productSkus);

    @Named("toMiniDto")
    InnerProductSkuResponse toMiniDto(ProductSku productSku);
    // Map từ DTO sang Entity
    ProductSku toEntity(ProductSkuResponse dto);
}
