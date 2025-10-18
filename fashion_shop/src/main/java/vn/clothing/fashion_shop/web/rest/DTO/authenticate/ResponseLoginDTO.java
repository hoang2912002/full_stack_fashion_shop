package vn.clothing.fashion_shop.web.rest.DTO.authenticate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.clothing.fashion_shop.domain.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseLoginDTO<T> {
    private String accessToken;
    private ResponseUserData user;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseUserData {
        private Long id;
        private String fullName;
        private String email;
        private String avatar;
        private Role role;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class UserGetAccount{
        private ResponseUserData user;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInsideToken {
        private Long id;
        private String fullName;
        private String email;
    }
}
