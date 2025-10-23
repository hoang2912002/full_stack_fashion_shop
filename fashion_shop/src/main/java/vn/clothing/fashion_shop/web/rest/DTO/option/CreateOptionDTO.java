package vn.clothing.fashion_shop.web.rest.DTO.option;

import java.time.Instant;
import java.util.List;

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
public class CreateOptionDTO {
    private Long id;
    private String name;
    private String slug;
    private String createdBy;
    private Instant createdAt;
    private boolean activated;
    private List<InnerCreateOptionValueDTO> optionValues;

    @Setter
    @Getter
    @NoArgsConstructor
    public static class InnerCreateOptionValueDTO {
        private Long id;
        private String name;
        private String slug;
        
    }
}
