package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.security.SecurityUtils;
import vn.clothing.fashion_shop.service.UserService;
import vn.clothing.fashion_shop.web.rest.DTO.authenticate.LoginDTO;
import vn.clothing.fashion_shop.web.rest.DTO.authenticate.ResponseLoginDTO;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticateController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtils securityUtils;
    private final UserService userService;

    public AuthenticateController(
        AuthenticationManagerBuilder authenticationManagerBuilder,
        SecurityUtils securityUtils,
        UserService userService
    ) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtils = securityUtils;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseLoginDTO> login (
        @RequestBody LoginDTO loginDTO
    ) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginDTO.getUsername(),
            loginDTO.getPassword()
        );
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User currentUserLogin = this.userService.handleGetUserByEmail(loginDTO.getUsername());
        ResponseLoginDTO<Object> responseLoginDTO = new ResponseLoginDTO<>();
        ResponseLoginDTO.ResponseUserData userDTO = new ResponseLoginDTO.ResponseUserData();
        if(currentUserLogin != null){
            BeanUtils.copyProperties(currentUserLogin, userDTO);
            String refresh_token = this.securityUtils.createRefreshToken(authentication.getName(),userDTO);
            this.userService.updateRefreshTokenUserByEmail(refresh_token, authentication.getName());
        }
        responseLoginDTO.setUser(userDTO);
        String access_token = this.securityUtils.createAccessToken(authentication.getName(),responseLoginDTO);
        responseLoginDTO.setAccessToken(access_token);


        return ResponseEntity.ok(responseLoginDTO);
    }
    
}
