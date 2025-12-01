package vn.clothing.fashion_shop.web.rest.DTO.responses;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.constants.enumEntity.GenderEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private Instant dob;
    private Integer age;
    private RoleResponse.InnerRoleResponse role;
    private List<AddressResponse.InnerAddressResponse> addresses;
    private Boolean activated;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class InnerUserResponse {
        private Long id;
        private String fullName;
        private Integer age;
        private String email;
        @Enumerated(EnumType.STRING)
        private GenderEnum gender;
    }
}
