package vn.clothing.fashion_shop.web.rest.DTO.responses;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.web.rest.DTO.responses.OptionResponse.InnerOptionResponse;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OptionValueResponse {
    private Long id;
    private String value;
    private String slug;
    private String createdBy;
    private Instant createdAt;
    private boolean activated;
    private Instant updatedAt;
    private boolean updatedBy;
    private InnerOptionResponse option;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class InnerOptionValueResponse {
        private Long id;
        private String value;
        private String slug;
    }
}
