package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.Product;
import vn.clothing.fashion_shop.web.rest.DTO.product.GetProductDTO;
import vn.clothing.fashion_shop.web.rest.DTO.product.ValidationProductDTO;

@Mapper(
    componentModel = "spring",
    uses = {
        ProductSkuMapper.class,
        CategoryMapper.class
    }
)
public interface ProductMapper extends EntityMapper<GetProductDTO, Product> {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "productSkus", ignore = true)
    // @Mapping(target= "price", numberFormat = "#,###")
    @Mapping(target = "price", expression = "java(String.format(\"%,.0f ₫\", product.getPrice()))")
    GetProductDTO toDto(Product product);
    List<GetProductDTO> toDto(List<Product> products);

    // Map từ DTO sang Entity
    Product toEntity(GetProductDTO dto);

    // @Mapping(target = "id", ignore = true)
    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "productSkus", ignore = true)
    @Mapping(target = "activated", constant = "true")
    Product toValidator(ValidationProductDTO dto);
        
}
