package vn.clothing.fashion_shop.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.UserResponse;

public interface UserService {
    User handleGetUserByEmail(String email);
    UserResponse createUser(User user);
    User updateRefreshTokenUserByEmail(String refresh_token, String email);
    User findRawUserById(Long id);
    UserResponse getUserById(Long id);
    UserResponse updateUser( User user);
    PaginationResponse getAllUser(Pageable pageable, Specification<User> spec);
    Void deleteUserById(Long id);
    User getCurrentUser();
}
