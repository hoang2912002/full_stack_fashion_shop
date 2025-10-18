package vn.clothing.fashion_shop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.clothing.fashion_shop.repository.AddressRepository;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }
    
}
