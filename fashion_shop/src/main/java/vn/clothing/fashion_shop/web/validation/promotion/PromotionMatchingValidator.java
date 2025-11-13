package vn.clothing.fashion_shop.web.validation.promotion;

import java.time.Instant;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.util.ValidatorField;
import vn.clothing.fashion_shop.web.rest.DTO.requests.PromotionRequest;
@Component
@RequiredArgsConstructor
public class PromotionMatchingValidator implements ConstraintValidator<PromotionMatching, PromotionRequest> {
    private final ValidatorField validatorField;
    @Override
    public boolean isValid(PromotionRequest value, ConstraintValidatorContext context) {
        boolean valid = true;
        if (value == null) {
            ValidatorField.addViolation(context, "promotion.data.notnull", "PromotionRequest");
            return false;
        }

        // --- BASIC FIELDS ---
        // &= là nếu false thì vẫn chạy tiếp khác với &&
        valid &= this.validatorField.checkNotBlank(value.getCode(), "promotion.code.notnull", "code", context);
        valid &= this.validatorField.checkNotBlank(value.getName(), "promotion.name.notnull", "name", context);
        valid &= this.validatorField.checkNotNull(value.getQuantity(), "promotion.quantity.notnull", "quantity", context);
        valid &= this.validatorField.checkNotNull(value.getDiscountType(), "promotion.discountType.notnull", "discountType", context);

        // --- DATE VALIDATION ---
        valid &= this.validatorField.checkNotNull(value.getStartDate(), "promotion.startDate.notnull", "startDate", context);
        valid &= this.validatorField.checkNotNull(value.getEndDate(), "promotion.endDate.notnull", "endDate", context);

        if (value.getStartDate() != null && value.getEndDate() != null &&
            value.getStartDate().isAfter(value.getEndDate())) {
            ValidatorField.addViolation(context, "promotion.startDate.before.endDate", "startDate");
            valid = false;
        }

        // --- OPTION LOGIC ---
        if (value.getOptionPromotion() != 0 && value.getOptionPromotion() != 1) {
            ValidatorField.addViolation(context, "promotion.optionPromotion.notformat", "optionPromotion");
            valid = false;
        } else if (value.getOptionPromotion() == 0) {
            valid &= this.validatorField.checkNotNull(value.getDiscountPercent(), "promotion.discountPercent.notnull", "discountPercent", context);
        } else {
            valid &= this.validatorField.checkNotNull(value.getMinDiscountAmount(), "promotion.minDiscountAmount.notnull", "minDiscountAmount", context);
            valid &= this.validatorField.checkNotNull(value.getMaxDiscountAmount(), "promotion.maxDiscountAmount.notnull", "maxDiscountAmount", context);
        }


        // --- UPDATE VALIDATION ---
        if (!value.isCreate()) {
            valid &= this.validatorField.checkNotNull(value.getId(), "promotion.id.notnull", "id", context);
        }
        return valid;
    }
}
