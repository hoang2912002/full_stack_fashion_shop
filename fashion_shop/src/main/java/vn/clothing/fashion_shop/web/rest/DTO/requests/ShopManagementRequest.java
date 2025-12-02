package vn.clothing.fashion_shop.web.rest.DTO.requests;

import java.time.Instant;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.clothing.fashion_shop.web.rest.DTO.requests.UserRequest.InnerUserRequest;
import vn.clothing.fashion_shop.web.validation.shop_management.ShopManagementMatching;
@NoArgsConstructor
@Data
@AllArgsConstructor
@ShopManagementMatching
public class ShopManagementRequest {
    private Long id;
    private String slug;
    private String name; // Tên gian hàng
    private String businessName; // Tên công ty
    private String businessNo; // Mã cty
    private Instant businessDateIssue; // Ngày thành lập
    private String businessPlace; // Địa chỉ cty
    private String taxCode; // Mã thuế
    private Integer businessType;
    private String accountName;
    private String accountNumber;
    private String bankName;
    private String bankBranch;
    private String logo;
    private String thumbnail;
    private String businessLicence;
    private String identificationImageFirst;
    private String identificationImageSecond;
    private String description;
    
    private String address;
    private InnerUserRequest user;

    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updateAt;
    private boolean activated;

    @JsonProperty("isCreate")
    private boolean isCreate;

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class InnerShopManagementRequest {
        private Long id;
    }
}
