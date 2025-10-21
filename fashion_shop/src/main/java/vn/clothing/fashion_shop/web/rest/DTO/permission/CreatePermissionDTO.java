package vn.clothing.fashion_shop.web.rest.DTO.permission;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePermissionDTO {
    private Long id;
    private String apiPath;
    private String method;
    private String module;
    private String name;
    private String createdBy;
    private Instant createdAt;
    private boolean activated;
}
