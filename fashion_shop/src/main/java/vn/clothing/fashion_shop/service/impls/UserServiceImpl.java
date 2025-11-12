package vn.clothing.fashion_shop.service.impls;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.constants.util.ConvertPagination;
import vn.clothing.fashion_shop.constants.util.MessageUtil;
import vn.clothing.fashion_shop.domain.Role;
import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.mapper.UserMapper;
import vn.clothing.fashion_shop.repository.UserRepository;
import vn.clothing.fashion_shop.service.AddressService;
import vn.clothing.fashion_shop.service.RoleService;
import vn.clothing.fashion_shop.service.UserService;
import vn.clothing.fashion_shop.web.rest.DTO.responses.AddressResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.UserResponse;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final AddressService addressService;
    private final MessageUtil messageUtil;
    private final UserMapper userMapper;

    @Override
    public User handleGetUserByEmail(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
        return user.isPresent() && !user.isEmpty() ? user.get() : null;
    }

    @Override
    @Transactional(rollbackOn= ServiceException.class)
    public UserResponse createUser(User user) {
        try {
            if (handleGetUserByEmail(user.getEmail()) != null) {
                throw new ServiceException(EnumError.USER_DATA_EXISTED_EMAIL,"user.exist.email",Map.of("email", user.getEmail()));
            }
            final Role role = Optional.ofNullable(user.getRole())
            .map(r -> this.roleService.handleGetRoleById(r.getId()))
            .orElse(null);

            final User userForCreate = User
                .builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .password(this.passwordEncoder.encode(user.getPassword()))
                .dob(user.getDob())
                .avatar(user.getAvatar())
                .age(user.getAge())
                .activated(true)
                .role(role)
                .build();
            final User savedUser = this.userRepository.save(userForCreate);
            
            final List<AddressResponse.InnerAddressResponse> addresses = 
                this.addressService.handleAddressesForUser(
                    savedUser, 
                    user.getAddresses(),
                    addr -> new AddressResponse.InnerAddressResponse(
                        addr.getId(), addr.getAddress(), addr.getCity(), addr.getDistrict(), addr.getWard()
                    )
                );
            final UserResponse createUserDTO = userMapper.toDto(savedUser);
            createUserDTO.setAddresses(addresses);
            return createUserDTO;
        } catch (Exception e) {
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    public User updateRefreshTokenUserByEmail(String refresh_token, String email) {
        final User user = handleGetUserByEmail(email);
        if (user == null) {
            throw new ServiceException(EnumError.USER_DATA_EXISTED_EMAIL, "user.exist.email",Map.of("email", email));
        }
        user.setRefreshToken(refresh_token);
        return this.userRepository.save(user);
    }

    @Override
    public User findRawUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ServiceException(EnumError.USER_ERR_NOT_FOUND_ID, "user.not.found.id",Map.of("id", id)));
    }

    @Override
    public UserResponse getUserById(Long id){
        try {
            final User user = findRawUserById(id);
            return userMapper.toDto(user);
        } catch (Exception e) {
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackOn= ServiceException.class)
    public UserResponse updateUser( User user) {
        try {
            final User updateUser = findRawUserById(user.getId());
            updateUser.setAge(user.getAge());
            updateUser.setAvatar(user.getAvatar());
            updateUser.setFullName(user.getFullName());

            Role role = Optional.ofNullable(user.getRole())
            .map(r -> this.roleService.handleGetRoleById(r.getId()))
            .orElse(null);

            updateUser.setRole(role);

            final List<AddressResponse.InnerAddressResponse> addresses = 
                this.addressService.handleAddressesForUser(
                    updateUser, user.getAddresses(),
                    addr -> new AddressResponse.InnerAddressResponse(
                        addr.getId(), addr.getAddress(), addr.getCity(), addr.getDistrict(), addr.getWard()
                    )
                );

            UserResponse updateUserDTO = userMapper.toDto(this.userRepository.saveAndFlush(updateUser));
            updateUserDTO.setAddresses(addresses);
            return updateUserDTO;
        } catch (Exception e) {
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackOn= ServiceException.class)
    public PaginationResponse getAllUser(Pageable pageable, Specification<User> spec){
        try {
            Page<User> users = this.userRepository.findAll(spec, pageable);
            List<UserResponse> userDTOs = users.getContent().stream().map(u -> {
                return userMapper.toDtoWithoutAddress(u);
            }).toList();

            return ConvertPagination.handleConvert(pageable, users, userDTOs);
        } catch (Exception e) {
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    public Void deleteUserById(Long id){
        User user = findRawUserById(id);
        this.userRepository.deleteById(id);
        return null;
    }
}
