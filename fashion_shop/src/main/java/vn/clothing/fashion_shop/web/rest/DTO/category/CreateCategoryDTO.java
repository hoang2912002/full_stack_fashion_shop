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
public class CreateCategoryDTO {
    private Long id;
    private String name;
    private String slug;
    private CreateCategoryDTO parent;
    private String createdBy;
    private Instant createdAt;
    private boolean activated;
}
