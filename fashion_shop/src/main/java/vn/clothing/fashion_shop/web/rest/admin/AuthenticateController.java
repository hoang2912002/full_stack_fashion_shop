package vn.clothing.fashion_shop.web.rest.admin;

import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.constants.util.MessageUtil;
import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.mapper.RoleMapper;
import vn.clothing.fashion_shop.security.SecurityUtils;
import vn.clothing.fashion_shop.service.AuthenticateService;
import vn.clothing.fashion_shop.service.UserService;
import vn.clothing.fashion_shop.web.rest.DTO.requests.LoginRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.LoginResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.LoginResponse.ResponseUserData;
import vn.clothing.fashion_shop.web.rest.DTO.responses.LoginResponse.UserGetAccount;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticateController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtils securityUtils;
    private final AuthenticateService authenticateService;
    private final UserService userService;
    private final RoleMapper roleMapper;
    private final MessageUtil messageUtil;
    Locale locale = LocaleContextHolder.getLocale();

    @Value("${fashionshop.cookie.path}")
    private String cookiePath;
    
    @Value("${fashionshop.cookie.sameSite}")
    private String cookieSameSite;
    
    @Value("${fashionshop.cookie.domain}")
    private String cookieDomain;

    @Value("${fashionshop.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;


    @PostMapping("/login")
    @ApiMessageResponse("auth.success.login")
    public ResponseEntity<LoginResponse> login (
        @RequestBody @Valid LoginRequest loginDTO
    ) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginDTO.getUsername(),
            loginDTO.getPassword()
        );
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User currentUserLogin = this.userService.handleGetUserByEmail(loginDTO.getUsername());
        LoginResponse responseLoginDTO = new LoginResponse();
        ResponseUserData userDTO = null;
        String refresh_token = "";
        if(currentUserLogin != null){
            userDTO = ResponseUserData.builder()
            .id(currentUserLogin.getId())
            .fullName(currentUserLogin.getFullName())
            .email(currentUserLogin.getEmail())
            .avatar(currentUserLogin.getAvatar())
            .role(roleMapper.toDto(currentUserLogin.getRole()))
            .build();
            refresh_token = this.authenticateService.createRefreshToken(authentication.getName(),userDTO);
            this.userService.updateRefreshTokenUserByEmail(refresh_token, authentication.getName());
        }
        responseLoginDTO.setUser(userDTO);
        String access_token = this.authenticateService.createAccessToken(authentication.getName(),responseLoginDTO.getUser());
        responseLoginDTO.setAccessToken(access_token);

        //Create cookie for refresh_token
        ResponseCookie springCookie = ResponseCookie.from("refresh_token", refresh_token)
            .httpOnly(true)
            .secure(true)
            .path(cookiePath)
            .maxAge(refreshTokenExpiration)
            .sameSite(cookieSameSite)
            .domain(cookieDomain)
            .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, springCookie.toString()).body(responseLoginDTO);
    }
    
    @GetMapping("/account")
    @ApiMessageResponse("auth.success.account")
    public ResponseEntity<ResponseUserData> getAccount() {
        String email = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        User user = this.userService.handleGetUserByEmail(email);
        if(user == null || user instanceof User == false){
            throw new ServiceException(EnumError.USER_ERR_NOT_FOUND_EMAIL, "user.not.found.email",Map.of("email", email));
        }

        // if(!user.isActivated()){
        //     throw new ServiceException(EnumError.USER_ERR_NOT_FOUND_EMAIL, "user.not.found.email",Map.of("email", email));
        //     throw new RuntimeException("Tài khoản " + email + " chưa được kích hoạt");
        // }
        LoginResponse response = new LoginResponse();
        UserGetAccount userDTO = response.new UserGetAccount();
        ResponseUserData responseUserData = ResponseUserData.builder()
            .id(user.getId())
            .fullName(user.getFullName())
            .email(user.getEmail())
            .avatar(user.getAvatar())
            .role(roleMapper.toDto(user.getRole()))
        .build();
        userDTO.setUser(responseUserData);
        return ResponseEntity.ok(responseUserData);
    }
    
    @PostMapping("/refresh_token")
    @ApiMessageResponse("auth.success.refresh.token")
    public ResponseEntity<LoginResponse> refreshToken(
        @CookieValue(name = "refresh_token", defaultValue = "") String refresh_token
    ) {
        Jwt token = this.securityUtils.getUserFromJWTToken(refresh_token);
        String email = token.getSubject();
        User currentUser = this.userService.handleGetUserByEmail(email);
        if(currentUser == null){
            throw new ServiceException(EnumError.USER_INVALID_REFRESH_TOKEN, "user.refresh.token.notformat",Map.of("refresh_token", "wrong"));

        }
        LoginResponse responseLoginDTO = new LoginResponse();
        ResponseUserData userDTO = ResponseUserData.builder()
            .id(currentUser.getId())
            .fullName(currentUser.getFullName())
            .email(currentUser.getEmail())
            .avatar(currentUser.getAvatar())
            .role(roleMapper.toDto(currentUser.getRole()))
        .build();
        responseLoginDTO.setUser(userDTO);
        String accessToken = this.authenticateService.createAccessToken(email,userDTO);
        responseLoginDTO.setAccessToken(accessToken);

        String new_refresh_token = this.authenticateService.createRefreshToken(email,userDTO);

        this.userService.updateRefreshTokenUserByEmail(new_refresh_token, email);
        
        ResponseCookie springCookie = ResponseCookie.from("refresh_token", new_refresh_token)
            .httpOnly(true)
            .secure(true)
            .path(cookiePath)
            .maxAge(refreshTokenExpiration)
            .sameSite(cookieSameSite)
            .domain(cookieDomain)
            .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, springCookie.toString()).body(responseLoginDTO);
    }
    
}
