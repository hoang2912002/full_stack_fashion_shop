package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.Permission;
import vn.clothing.fashion_shop.web.rest.DTO.requests.PermissionRequest;
import vn.clothing.fashion_shop.web.rest.DTO.requests.RoleRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PermissionResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.RoleResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PermissionResponse.InnerPermissionResponse;

@Mapper(
    componentModel = "spring",
    uses={
    }    
)
public interface PermissionMapper extends EntityMapper<PermissionResponse, Permission>{
    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);

    @Named("toDto")
    PermissionResponse toDto(Permission permission);
    List<PermissionResponse> toDto(List<Permission> permissions);

    @Named("toMiniDto")
    InnerPermissionResponse toMiniDto(Permission permission);
    List<InnerPermissionResponse> toMiniDto(List<Permission> permissions);


    Permission toRequest(PermissionRequest dto);

    Permission toEntity(PermissionResponse dto);
}
