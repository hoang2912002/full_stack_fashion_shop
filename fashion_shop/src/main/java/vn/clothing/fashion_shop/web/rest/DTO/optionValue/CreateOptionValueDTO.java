package vn.clothing.fashion_shop.web.rest.DTO.optionValue;

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
public class CreateOptionValueDTO {
    private Long id;
    private String value;
    private String slug;
    private String createdBy;
    private Instant createdAt;
    private boolean activated;
}
