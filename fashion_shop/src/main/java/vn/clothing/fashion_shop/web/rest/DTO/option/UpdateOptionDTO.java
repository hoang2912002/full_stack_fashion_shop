package vn.clothing.fashion_shop.web.rest.DTO.option;

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
public class UpdateOptionDTO {
    private Long id;
    private String name;
    private String slug;
    private boolean activated;
    private Instant updatedAt;
    private boolean updatedBy;
}
