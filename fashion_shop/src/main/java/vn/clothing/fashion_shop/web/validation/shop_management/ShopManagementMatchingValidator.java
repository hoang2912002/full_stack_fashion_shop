package vn.clothing.fashion_shop.web.validation.shop_management;

import java.time.Instant;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.util.ValidatorField;
import vn.clothing.fashion_shop.domain.ShopManagement;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ShopManagementRequest;

@Component
@RequiredArgsConstructor
public class ShopManagementMatchingValidator implements ConstraintValidator<ShopManagementMatching, ShopManagementRequest> {
    private final ValidatorField validatorField;
    @Override
    public boolean isValid(ShopManagementRequest value, ConstraintValidatorContext context) {
        boolean valid = true;
        if (value == null) {
            ValidatorField.addViolation(context, "shop.management.data.notnull", "ShopManagementRequest");
            return false;
        }
        // --- BASIC FIELDS ---
        // &= là nếu false thì vẫn chạy tiếp khác với &&
        valid &= this.validatorField.checkNotBlank(value.getAccountName(), "shop.management.accountName.notnull", "accountName", context);
        valid &= this.validatorField.checkNotBlank(value.getAccountNumber(), "shop.management.accountNumber.notnull", "accountNumber", context);
        valid &= this.validatorField.checkNotBlank(value.getAddress(), "shop.management.address.notnull", "address", context);
        valid &= this.validatorField.checkNotBlank(value.getBankBranch(), "shop.management.bankBranch.notnull", "bankBranch", context);
        valid &= this.validatorField.checkNotBlank(value.getBankName(), "shop.management.bankName.notnull", "bankName", context);
        valid &= this.validatorField.checkNotBlank(value.getName(), "shop.management.user.id.notnull", "name", context);

        // --- DATE VALIDATION ---
        if(value.getBusinessDateIssue() != null && value.getBusinessDateIssue().compareTo(Instant.now()) > 0 ){
            ValidatorField.addViolation(context, "shop.management.businessDateIssue.before.now", "businessDateIssue");
            return false;
        }
        valid &= this.validatorField.checkNotNull(value.getUser().getId(), "shop.management.user.id.notnull", "user", context);
        // --- UPDATE VALIDATION ---
        if (!value.isCreate()) {
            valid &= this.validatorField.checkNotNull(value.getId(), "shop.management.id.notnull", "id", context);
        }
        return valid;
    }
    
}
