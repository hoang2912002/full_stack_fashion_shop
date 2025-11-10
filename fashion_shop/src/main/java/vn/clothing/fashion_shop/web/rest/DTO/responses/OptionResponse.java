package vn.clothing.fashion_shop.web.rest.DTO.responses;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.web.rest.DTO.responses.OptionValueResponse.InnerOptionValueResponse;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OptionResponse {
    private Long id;
    private String name;
    private String slug;
    private String createdBy;
    private Instant createdAt;
    private boolean activated;
    private Instant updatedAt;
    private String updatedBy;
    private List<InnerOptionValueResponse> optionValues;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class InnerOptionResponse {
        private Long id;
        private String name;
        private String slug;
    }
}
