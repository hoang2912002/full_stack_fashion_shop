package vn.clothing.fashion_shop.web.rest.DTO.responses;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
