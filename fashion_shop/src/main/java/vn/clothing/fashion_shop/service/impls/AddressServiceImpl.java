package vn.clothing.fashion_shop.service.impls;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.domain.Address;
import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.repository.AddressRepository;
import vn.clothing.fashion_shop.service.AddressService;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;
@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    @Override
    @Transactional(readOnly=true)
    public Address getRawAddressById(Long id) {
        try {
            Optional<Address> address = this.addressRepository.findById(id);
            return address.isPresent() ? address.get() : null;
        } catch (Exception e) {
            log.error("[getRawAddressById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    //Để bên ngoài gọi, trong cùng class không hoạt động
    @Transactional(rollbackFor=ServiceException.class)
    public Address saveAddress(Address address) {
        try {
            return this.addressRepository.save(address);
        } catch (Exception e) {
            log.error("[saveAddress] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
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

    @Override
    @Transactional(rollbackFor=ServiceException.class)
    public <T> List<T> handleAddressesForUser(
        User updateUser, 
        List<Address> inputAddresses,
        Function<Address, T> mapper
    ) {
        try {
            final List<T> result = new ArrayList<>();
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
        } catch (Exception e) {
            log.error("[handleAddressesForUser] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
        
    }
}
