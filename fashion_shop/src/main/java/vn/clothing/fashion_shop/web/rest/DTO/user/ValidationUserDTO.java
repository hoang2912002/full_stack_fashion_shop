package vn.clothing.fashion_shop.web.rest.DTO.user;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import vn.clothing.fashion_shop.constants.GenderEnum;
import vn.clothing.fashion_shop.domain.Address;
import vn.clothing.fashion_shop.domain.Role;
import vn.clothing.fashion_shop.web.validation.user.UserMatching;

@Setter
@Getter
@UserMatching
public class ValidationUserDTO {
    private Long id;
    private String fullName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private Instant dob;
    private Integer age;
    private Role role;
    private List<Address> addresses;
    private boolean activated;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    @JsonProperty("isCreate")
    private boolean isCreate;
}
