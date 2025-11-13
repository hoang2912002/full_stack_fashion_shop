package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.Product;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ProductRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ProductResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ProductResponse.InnerProductResponse;

@Mapper(
    componentModel = "spring",
    uses = {
        ProductSkuMapper.class,
        CategoryMapper.class,
        OptionMapper.class,
        OptionValueMapper.class
    }
)
public interface ProductMapper extends EntityMapper<ProductResponse, Product> {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Named("toDto")
    @Mapping(target = "productSkus", ignore = true)
    @Mapping(target = "options", ignore = true)
    @Mapping(target = "optionValues", ignore = true)
    @Mapping(target = "price", expression = "java(String.format(\"%,.0f ₫\", product.getPrice()))")
    ProductResponse toDto(Product product);

    //ép MapStruct chỉ dùng hàm
    @IterableMapping(qualifiedByName = "toDto")
    List<ProductResponse> toDto(List<Product> products);

    Product toEntity(ProductResponse dto);

    @Named("toMiniDto")
    InnerProductResponse toMinDto(Product product);

    @Named("toDetailDto")
    @Mapping(target = "price", expression = "java(String.format(\"%,.0f ₫\", product.getPrice()))")
    ProductResponse detailDto(Product product);

    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "productSkus", ignore = true)
    @Mapping(target = "activated", constant = "true")
    Product toValidator(ProductRequest dto);
        
}
