package vn.clothing.fashion_shop.web.rest.DTO.authenticate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.clothing.fashion_shop.web.validation.Login.LoginMatching;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@LoginMatching
public class LoginDTO {
    private String username;
    private String password;
    
    @Override
    public String toString() {
        return "LoginDTO [username=" + username + ", password=" + password + "]";
    }
}
