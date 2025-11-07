package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.Address;
import vn.clothing.fashion_shop.web.rest.DTO.responses.AddressResponse;

public interface AddressMapper extends EntityMapper<AddressResponse, Address> {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    @Named("toDto")
    AddressResponse toDto(Address address);
    List<AddressResponse> toDto(List<Address> addresses);

    @Named("toMiniDto")
    AddressResponse.InnerAddressResponse toMiniDto(Address address);


    Address toRequest(AddressResponse address);

    Address toEntity(AddressResponse address);
}
