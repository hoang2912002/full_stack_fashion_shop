package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.Product;
import vn.clothing.fashion_shop.domain.Promotion;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ProductRequest;
import vn.clothing.fashion_shop.web.rest.DTO.requests.PromotionRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PromotionResponse;

@Mapper(
    componentModel = "spring",
    uses = {
        ProductMapper.class,
        CategoryMapper.class
    }
)
public interface PromotionMapper extends EntityMapper<PromotionResponse, Promotion> {
    PromotionMapper INSTANCE = Mappers.getMapper(PromotionMapper.class);
    
    @Named("toDto")
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "categories", ignore = true)
    PromotionResponse toDto(Promotion promotion);
    List<PromotionResponse> toDto(List<Promotion> promotions);

    Promotion toEntity(PromotionResponse dto);

    @Named("toDetailDto")
    PromotionResponse detailDto(Promotion promotion);

    Promotion toValidator(PromotionRequest dto);
}
