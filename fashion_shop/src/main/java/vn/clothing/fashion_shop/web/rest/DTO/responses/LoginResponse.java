package vn.clothing.fashion_shop.web.rest.DTO.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.clothing.fashion_shop.domain.Role;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LoginResponse {
    private String accessToken;
    private ResponseUserData user;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseUserData {
        private Long id;
        private String fullName;
        private String email;
        private String avatar;
        private RoleResponse role;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class UserGetAccount{
        private ResponseUserData user;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInsideToken {
        private Long id;
        private String fullName;
        private String email;
    }
}
