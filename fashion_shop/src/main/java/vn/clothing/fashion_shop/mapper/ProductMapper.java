package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.Product;
import vn.clothing.fashion_shop.web.rest.DTO.product.GetProductDTO;

@Mapper(
    componentModel = "spring",
    uses = {
        ProductSkuMapper.class
    }
)
public interface ProductMapper extends EntityMapper<GetProductDTO, Product> {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    GetProductDTO toDto(Product product);
    List<GetProductDTO> toDto(List<Product> products);

    // Map từ DTO sang Entity
    Product toEntity(GetProductDTO dto);
        
}
