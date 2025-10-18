package vn.clothing.fashion_shop.web.rest.DTO.user;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDTO {
    private String fullName;
    private String email;
    @Enumerated(EnumType.STRING)
    private Enum gender;
    private Integer age;
    private Instant createdAt;
    private String createdBy;
    private List<InnerAddressDTO> addresses;
    private InnerRoleDTO role;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public class InnerAddressDTO {
        private Long id;
        private String address;
        private String city;
        private String district;
        private String ward;
    }
   
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public class InnerRoleDTO {
        private Long id;
        private String name;
        private String slug;
    }
}
