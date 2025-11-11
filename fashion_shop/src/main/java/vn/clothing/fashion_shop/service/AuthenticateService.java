package vn.clothing.fashion_shop.service;

import vn.clothing.fashion_shop.web.rest.DTO.responses.LoginResponse.ResponseUserData;

public interface AuthenticateService {
    String createAccessToken(String email, ResponseUserData responseLoginDTO);

    String createRefreshToken(String email, ResponseUserData responseLoginDTO);
}
