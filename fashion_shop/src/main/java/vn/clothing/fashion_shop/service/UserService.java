package vn.clothing.fashion_shop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import vn.clothing.fashion_shop.domain.Address;
import vn.clothing.fashion_shop.domain.Role;
import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.repository.UserRepository;
import vn.clothing.fashion_shop.web.rest.DTO.user.CreateUserDTO;
import vn.clothing.fashion_shop.web.rest.DTO.user.GetUserDTO;
import vn.clothing.fashion_shop.web.rest.DTO.user.UpdateUserDTO;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final AddressService addressService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService,
            AddressService addressService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.addressService = addressService;
    }

    public User handleGetUserByEmail(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
        return user.isPresent() && user != null ? user.get() : null;
    }

    public CreateUserDTO createUser(User user) {
        User checkUser = handleGetUserByEmail(user.getEmail());
        if (checkUser != null || checkUser instanceof User) {
            throw new RuntimeException("Người dùng với email: " + user.getEmail() + " đã tồn tại");
        }
        Role role = new Role();
        List<Address> addresses = new ArrayList<>();
        if (user.getRole() != null) {
            role = this.roleService.handleGetRoleById(user.getRole().getId());
        }

        if (user.getAddresses() != null && user.getAddresses().size() > 0) {
            addresses = user.getAddresses().stream().map(
                    a -> {
                        Address address = new Address();
                        BeanUtils.copyProperties(a, address);
                        return address;
                    }).toList();
        } else {
            role = null;
        }
        User userForCreate = new User();
        BeanUtils.copyProperties(user, userForCreate);
        userForCreate.setRole(role);
        userForCreate.setActivated(true);
        userForCreate.setPassword(this.passwordEncoder.encode(user.getPassword()));
        User userAfterCreate = this.userRepository.save(userForCreate);
        CreateUserDTO createUserDTO = new CreateUserDTO();
        CreateUserDTO.InnerRoleDTO roleDTO = createUserDTO.new InnerRoleDTO();
        BeanUtils.copyProperties(userAfterCreate, createUserDTO);
        BeanUtils.copyProperties(role == null ? new Role() : role, roleDTO);
        createUserDTO.setRole(roleDTO);
        return createUserDTO;

    }

    public User updateRefreshTokenUserByEmail(String refresh_token, String email) {
        User user = handleGetUserByEmail(email);
        if (user == null) {
            throw new RuntimeException("Người dùng với email: " + email + " đã tồn tại");
        }
        user.setRefreshToken(refresh_token);
        return this.userRepository.save(user);
    }

    public User findRawUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với id: " + id));
    }

    public GetUserDTO getUserById(Long id){
        User user = findRawUserById(id);
        GetUserDTO userDTO = new GetUserDTO();
        GetUserDTO.InnerRoleDTO roleDTO = userDTO. new InnerRoleDTO();
        BeanUtils.copyProperties(user, userDTO);
        if(user.getRole() != null ){
            BeanUtils.copyProperties(user.getRole(), roleDTO);
        }
        userDTO.setRole(roleDTO);
        return userDTO;
    }

    public UpdateUserDTO updateUser( User user) {
        User updateUser = findRawUserById(user.getId());
        updateUser.setAge(user.getAge());
        updateUser.setAvatar(user.getAvatar());
        updateUser.setFullName(user.getFullName());

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        UpdateUserDTO.InnerRoleDTO roleDTO = updateUserDTO.new InnerRoleDTO();

        Role role = (user.getRole() != null) 
            ? this.roleService.handleGetRoleById(user.getRole().getId()) : null;
        updateUser.setRole(role);

        List<UpdateUserDTO.InnerAddressDTO> addresses = this.addressService.handleAddressesForUser(updateUser, user.getAddresses());
        // updateUser.setAddresses(addresses);
        updateUser = this.userRepository.saveAndFlush(updateUser);
        
        BeanUtils.copyProperties(updateUser, updateUserDTO);
        BeanUtils.copyProperties(role != null ? role : roleDTO, roleDTO);
        updateUserDTO.setRole(roleDTO);
        updateUserDTO.setAddresses(addresses);
        return updateUserDTO;

    }
}
