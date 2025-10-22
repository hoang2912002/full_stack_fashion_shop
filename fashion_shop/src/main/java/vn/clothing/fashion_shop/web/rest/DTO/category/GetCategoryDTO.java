package vn.clothing.fashion_shop.web.rest.DTO.category;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCategoryDTO {
    private Long id;
    private String name;
    private String slug;
    private List<InnerGetCategoryDTO> children;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private boolean activated;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InnerGetCategoryDTO {
        private Long id;
        private String name;
        private String slug;
    }
}
