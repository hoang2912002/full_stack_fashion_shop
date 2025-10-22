package vn.clothing.fashion_shop.web.rest.DTO.category;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.clothing.fashion_shop.domain.Category;
import vn.clothing.fashion_shop.web.validation.category.CategoryMatching;

@CategoryMatching
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationCategoryDTO {
    private Long id;
    private String name;
    private String slug;
    private Category parent;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private boolean activated;
    @JsonProperty("isCreate")
    private boolean isCreate;
}
