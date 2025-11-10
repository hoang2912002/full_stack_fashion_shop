package vn.clothing.fashion_shop.web.rest.DTO.requests;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.web.rest.DTO.requests.OptionRequest.InnerOptionRequest;
import vn.clothing.fashion_shop.web.validation.optionValue.OptionValueMatching;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@OptionValueMatching
public class OptionValueRequest {
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

    private InnerOptionRequest option;

    public static class InnerOptionValueRequest{
        private Long id;
    }
}
