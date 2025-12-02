package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.Promotion;
import vn.clothing.fashion_shop.domain.ShopManagement;
import vn.clothing.fashion_shop.web.rest.DTO.requests.PromotionRequest;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ShopManagementRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ShopManagementResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ShopManagementResponse.InnerShopManagementResponse;
@Mapper(
    componentModel = "spring",
    uses={
        UserMapper.class
    }    
)
public interface ShopManagementMapper extends EntityMapper<ShopManagementResponse, ShopManagement> {
    ShopManagementMapper INSTANCE = Mappers.getMapper(ShopManagementMapper.class);
    
    @Named("toDto")
    ShopManagementResponse toDto(ShopManagement shopManagement);
    List<ShopManagementResponse> toDto(List<ShopManagement> shopManagements);

    
    @Named("toMiniDto")
    InnerShopManagementResponse toMiniDto(ShopManagement shopManagement);
    
    ShopManagement toValidator(ShopManagementRequest dto);
    ShopManagement toEntity(ShopManagementResponse dto);
}
