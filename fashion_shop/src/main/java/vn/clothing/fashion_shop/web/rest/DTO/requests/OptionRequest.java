package vn.clothing.fashion_shop.web.rest.DTO.requests;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.web.validation.option.OptionMatching;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@OptionMatching
public class OptionRequest {
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InnerOptionRequest {
        private Long id;
    }
}
