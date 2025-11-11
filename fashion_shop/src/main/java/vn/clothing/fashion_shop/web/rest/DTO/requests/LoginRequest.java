package vn.clothing.fashion_shop.web.rest.DTO.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.web.validation.login.LoginMatching;

@NoArgsConstructor
@AllArgsConstructor
@LoginMatching
@Data
@Builder
public class LoginRequest {
    private String username;
    private String password;
}
