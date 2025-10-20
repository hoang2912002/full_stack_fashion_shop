package vn.clothing.fashion_shop.web.rest.DTO.permission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetPermissionDTO {
    private String apiPath;
    private String method;
    private String module;
    private String name;

}
