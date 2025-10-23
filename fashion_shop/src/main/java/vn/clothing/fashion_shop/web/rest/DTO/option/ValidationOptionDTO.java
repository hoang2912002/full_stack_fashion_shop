package vn.clothing.fashion_shop.web.rest.DTO.option;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.clothing.fashion_shop.web.validation.option.OptionMatching;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@OptionMatching
public class ValidationOptionDTO {
    private Long id;
    private String name;
    private String slug;
    private String createdBy;
    private Instant createdAt;
    private boolean activated;
    private Instant updatedAt;
    private String updatedBy;
    @JsonProperty("isCreate")
    private boolean isCreate;
}
