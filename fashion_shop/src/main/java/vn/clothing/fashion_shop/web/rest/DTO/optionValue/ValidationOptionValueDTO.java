package vn.clothing.fashion_shop.web.rest.DTO.optionValue;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.clothing.fashion_shop.web.validation.optionValue.OptionValueMatching;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@OptionValueMatching
public class ValidationOptionValueDTO {
    private Long id;
    private String value;
    private String slug;
    private String createdBy;
    private Instant createdAt;
    private boolean activated;
    private Instant updatedAt;
    private String updatedBy;
    @JsonProperty("isCreate")
    private boolean isCreate;

    private InnerOptionValueDTO option;

    public static class InnerOptionValueDTO{
        private Long id;
    }
}
