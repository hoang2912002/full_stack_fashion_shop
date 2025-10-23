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
public class GetOptionDTO {
    private Long id;
    private String name;
    private String slug;
    private String createdBy;
    private Instant createdAt;
    private boolean activated;
    private Instant updatedAt;
    private boolean updatedBy;

    @Setter
    @Getter
    @NoArgsConstructor
    public static class InnerOptionValueDTO {
        private Long id;
        private String name;
        private String slug;
        
    }
}
