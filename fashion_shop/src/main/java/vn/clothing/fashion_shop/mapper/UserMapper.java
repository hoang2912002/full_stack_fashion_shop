package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.web.rest.DTO.requests.UserRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.UserResponse;

@Mapper(
    componentModel = "spring",
    uses={
        RoleMapper.class
    }    
)
public interface UserMapper extends EntityMapper<UserResponse, User>{
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Named("toDto")
    UserResponse toDto(User user);
    List<UserResponse> toDto(List<User> users);

    @Named("toDtoWithoutAddress")
    @Mapping(target = "addresses", ignore=true)
    UserResponse toDtoWithoutAddress(User user);

    @Named("toMiniDto")
    UserResponse toMiniDto(User user);

    
    User toRequest(UserRequest user);

    User toEntity(UserResponse dto);
}
