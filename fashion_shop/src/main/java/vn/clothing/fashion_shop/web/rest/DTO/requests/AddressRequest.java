package vn.clothing.fashion_shop.web.rest.DTO.requests;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
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
    @JsonProperty("isCreate")
    private boolean isCreate;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InnerAddressRequest{
        private Long id;
        private String address;
        private String city;
        private String district;
        private String ward;
    }
}
