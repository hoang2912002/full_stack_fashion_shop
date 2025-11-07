package vn.clothing.fashion_shop.web.rest.DTO.responses;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponse {
    private Long id;
    private String address;
    private String city;
    private String district;
    private String ward;
    private Boolean activated;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class InnerAddressResponse {
        private Long id;
        private String address;
        private String city;
        private String district;
        private String ward;
    }
}
