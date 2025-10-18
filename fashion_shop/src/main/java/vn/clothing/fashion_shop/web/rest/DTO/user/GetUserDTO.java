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
public class GetUserDTO {
    private Long id;
    private String fullName;
    private String email;
    @Enumerated(EnumType.STRING)
    private Enum gender;
    private Instant dob;
    private Integer age;
    private InnerRoleDTO role;
    private Boolean activated;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    

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
