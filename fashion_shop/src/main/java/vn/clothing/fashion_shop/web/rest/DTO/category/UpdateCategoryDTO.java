package vn.clothing.fashion_shop.web.rest.DTO.category;

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
public class UpdateCategoryDTO {
    private Long id;
    private String name;
    private String slug;
    private UpdateCategoryDTO parent;
    private String updatedBy;
    private Instant updatedAt;
    private boolean activated;
}
