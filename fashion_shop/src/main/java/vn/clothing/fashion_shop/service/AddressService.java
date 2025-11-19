package vn.clothing.fashion_shop.service;

import java.util.List;
import java.util.function.Function;

import vn.clothing.fashion_shop.domain.Address;
import vn.clothing.fashion_shop.domain.User;

public interface AddressService {
    Address getRawAddressById(Long id);

    Address saveAddress(Address address);

    <T> List<T> handleAddressesForUser(User updateUser, List<Address> inputAddresses,Function<Address, T> mapper);
}
