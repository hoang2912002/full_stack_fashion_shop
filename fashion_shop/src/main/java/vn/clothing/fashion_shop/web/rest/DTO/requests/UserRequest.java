package vn.clothing.fashion_shop.web.rest.DTO.requests;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.constants.enumEntity.GenderEnum;
import vn.clothing.fashion_shop.domain.Address;
import vn.clothing.fashion_shop.domain.Role;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class UserRequest {
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
