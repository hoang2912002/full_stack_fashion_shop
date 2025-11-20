package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.Inventory;
import vn.clothing.fashion_shop.web.rest.DTO.requests.InventoryRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.InventoryResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.InventoryResponse.InnerInventoryResponse;

@Mapper(
    componentModel = "spring"
)
public interface InventoryMapper extends EntityMapper<InventoryResponse,Inventory>{
    InventoryMapper INSTANCE = Mappers.getMapper(InventoryMapper.class);

    @Named("toDto")
    InventoryResponse toDto(Inventory inventory);

    @IterableMapping(qualifiedByName = "toDto")
    List<InventoryResponse> toDto(List<Inventory> inventories);

    @Named("toMiniDto")
    InnerInventoryResponse toMiniDto(Inventory inventory);

    @Named("toMiniDto")
    @IterableMapping(qualifiedByName = "toMiniDto")
    List<InnerInventoryResponse> toMiniDto(List<Inventory> inventories);
    
    Inventory toValidator(InventoryRequest dto);
    Inventory toEntity(InventoryResponse dto);
}
