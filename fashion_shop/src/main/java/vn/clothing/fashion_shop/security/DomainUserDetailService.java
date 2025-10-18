package vn.clothing.fashion_shop.security;

import java.io.NotActiveException;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import vn.clothing.fashion_shop.domain.Role;
import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.service.UserService;

@Component("userDetailsService")
public class DomainUserDetailService implements UserDetailsService {
    private final UserService userService;

    public DomainUserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userService.handleGetUserByEmail(username);
        if(user == null){
            throw new UsernameNotFoundException("Tài khoản/mật khẩu không hợp lệ");
        }
        if (!user.isActivated()) {
            throw new UsernameNotFoundException("Tài khoản " + username + " không được actived");
        }
        String role = "USER";
        if(user.getRole() instanceof Role){
            role = user.getRole().getSlug().toUpperCase();
        }
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role ))
        );
    }
    

}
