package vn.clothing.fashion_shop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import vn.clothing.fashion_shop.domain.Address;
import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.repository.AddressRepository;
import vn.clothing.fashion_shop.web.rest.DTO.user.UpdateUserDTO;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address getRawAddressById(Long id) {
        Optional<Address> address = this.addressRepository.findById(id);
        return address.isPresent() && address != null ? address.get() : null;
    }

    public Address saveAddress(Address address) {
        return this.addressRepository.save(address);
    }

    private void updateAddressData(Address target, Address source) {
        target.setAddress(source.getAddress());
        target.setCity(source.getCity());
        target.setDistrict(source.getDistrict());
        target.setWard(source.getWard());
    }

    public Address convertDataForUpdateUser(Address address) {
        Address newAddress = new Address();
        updateAddressData(newAddress, address);
        newAddress.setUser(address.getUser());
        newAddress.setId(address.getId());
        return newAddress;
    }

    public <T> List<T> handleAddressesForUser(
        User updateUser, 
        List<Address> inputAddresses,
        Function<Address, T> mapper
    ) {
        List<T> result = new ArrayList<>();
        // UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        if (inputAddresses == null)
            return result;

        for (Address addr : inputAddresses) {
            Address currentAddress;

            if (addr.getId() != null) {
                // 🧩 Update address cũ
                currentAddress = getRawAddressById(addr.getId());
                if (currentAddress == null) {
                    currentAddress = new Address();
                }
            } else {
                // 🧩 Tạo mới
                currentAddress = new Address();
            }
            updateAddressData(currentAddress, addr);

            currentAddress.setActivated(true);
            currentAddress.setUser(updateUser);
            currentAddress = saveAddress(currentAddress);
            result.add(mapper.apply(currentAddress));
        }

        return result;
    }

}
