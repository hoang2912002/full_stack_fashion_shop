package vn.clothing.fashion_shop.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User handleGetUserByEmail(String email){
        Optional<User> user = this.userRepository.findByEmail(email);
        return user.isPresent() && user != null ? user.get() : null;
    }

    public User createUser(User user){
        User checkUser = handleGetUserByEmail(user.getEmail());
        if(checkUser != null || checkUser instanceof User){
            throw new RuntimeException("Người dùng với email: " + user.getEmail() + " đã tồn tại");
        }
        User userForCreate = new User();
        BeanUtils.copyProperties(user, userForCreate);
        userForCreate.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(userForCreate);
    }

    public User updateRefreshTokenUserByEmail(String refresh_token, String email){
        User user = handleGetUserByEmail(email);
        if(user == null){
            throw new RuntimeException("Người dùng với email: " + email + " đã tồn tại");
        }
        user.setRefreshToken(refresh_token);
        return this.userRepository.save(user);
    }
}
