package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.clothing.fashion_shop.constants.ApiResponse;
import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.security.SecurityUtils;
import vn.clothing.fashion_shop.service.UserService;
import vn.clothing.fashion_shop.web.rest.DTO.authenticate.LoginDTO;
import vn.clothing.fashion_shop.web.rest.DTO.authenticate.ResponseLoginDTO;

import java.time.Instant;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
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


@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticateController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtils securityUtils;
    private final UserService userService;

    @Value("${fashionshop.cookie.path}")
    private String cookiePath;
    
    @Value("${fashionshop.cookie.sameSite}")
    private String cookieSameSite;
    
    @Value("${fashionshop.cookie.domain}")
    private String cookieDomain;

    @Value("${fashionshop.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

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
    @ApiMessageResponse("Đăng nhập thành công")
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
        String refresh_token = "";
        if(currentUserLogin != null){
            BeanUtils.copyProperties(currentUserLogin, userDTO);
            refresh_token = this.securityUtils.createRefreshToken(authentication.getName(),userDTO);
            this.userService.updateRefreshTokenUserByEmail(refresh_token, authentication.getName());
        }
        responseLoginDTO.setUser(userDTO);
        String access_token = this.securityUtils.createAccessToken(authentication.getName(),responseLoginDTO);
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
    @ApiMessageResponse("Lấy thông tin tài khoản thành công")
    public ResponseEntity<ResponseLoginDTO.ResponseUserData> getAccount() {
        String email = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        User user = this.userService.handleGetUserByEmail(email);
        if(user == null || user instanceof User == false){
            throw new RuntimeException("Người dùng với email: " + user.getEmail() + " đã tồn tại");
        }

        if(!user.isActivated()){
            throw new RuntimeException("Tài khoản " + email + " chưa được kích hoạt");
        }
        ResponseLoginDTO<Object> response = new ResponseLoginDTO<>();
        ResponseLoginDTO.UserGetAccount userDTO = response.new UserGetAccount();
        ResponseLoginDTO.ResponseUserData responseUserData = new ResponseLoginDTO.ResponseUserData();
        BeanUtils.copyProperties(user, responseUserData);
        userDTO.setUser(responseUserData);
        return ResponseEntity.ok(responseUserData);
    }
    
    @PostMapping("/refresh_token")
    @ApiMessageResponse("Refresh token thành công")
    public ResponseEntity<ResponseLoginDTO> refreshToken(
        @CookieValue(name = "refresh_token", defaultValue = "") String refresh_token
    ) {
        Jwt token = this.securityUtils.getUserFromJWTToken(refresh_token);
        if(token.getExpiresAt().isBefore(Instant.now())){
            throw new RuntimeException("Hết hạn đăng nhập, vui lòng đăng nhập lại");
        }
        String email = token.getSubject();
        User currentUser = this.userService.handleGetUserByEmail(email);
        if(currentUser == null){
            throw new RuntimeException("Refresh token không hợp lệ");
        }
        ResponseLoginDTO<Object> responseLoginDTO = new ResponseLoginDTO<>();
        ResponseLoginDTO.ResponseUserData userDTO = new ResponseLoginDTO.ResponseUserData();
        BeanUtils.copyProperties(currentUser, userDTO);
        responseLoginDTO.setUser(userDTO);
        String accessToken = this.securityUtils.createAccessToken(email,responseLoginDTO);
        responseLoginDTO.setAccessToken(accessToken);

        String new_refresh_token = this.securityUtils.createRefreshToken(email,userDTO);

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
