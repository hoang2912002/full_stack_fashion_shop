package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.Role;
import vn.clothing.fashion_shop.web.rest.DTO.responses.RoleResponse;

@Mapper(
    componentModel = "spring",
    uses={
    }    
)
public interface RoleMapper extends EntityMapper<RoleResponse, Role>{
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    @Named("toDto")
    RoleResponse toDto(Role role);
    List<RoleResponse> toDto(List<Role> roles);

    @Named("toMiniDto")
    RoleResponse.InnerRoleResponse toMiniDto(Role role);
    List<RoleResponse.InnerRoleResponse> toMiniDto(List<Role> roles);

    Role toEntity(RoleResponse dto);
}
