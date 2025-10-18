package vn.clothing.fashion_shop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.clothing.fashion_shop.domain.Address;
import vn.clothing.fashion_shop.repository.AddressRepository;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }
    
    public Address getRawAddressById(Long id){
        return this.addressRepository.findById(id).orElseThrow(() -> null);
    }

    public Address saveAddress(Address address){
        return this.addressRepository.save(address);
    }
}
