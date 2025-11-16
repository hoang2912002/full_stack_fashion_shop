package vn.clothing.fashion_shop.web.validation.coupon;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.enumEntity.CouponEnum;
import vn.clothing.fashion_shop.constants.util.ValidatorField;
import vn.clothing.fashion_shop.web.rest.DTO.requests.CouponRequest;


@Component
@RequiredArgsConstructor
public class CouponMatchingValidator implements ConstraintValidator<CouponMatching, CouponRequest> {
    private final ValidatorField validatorField;
    @Override
    public boolean isValid(CouponRequest value, ConstraintValidatorContext context) {
        boolean valid = true;
        if (value == null) {
            ValidatorField.addViolation(context, "coupon.data.notnull", "CouponRequest");
            return false;
        }

        // --- BASIC FIELDS ---
        valid &= this.validatorField.checkNotBlank(value.getCode(), "coupon.code.notnull", "code", context);
        valid &= this.validatorField.checkNotBlank(value.getName(), "coupon.name.notnull", "name", context);
        valid &= this.validatorField.checkNotNull(value.getCouponAmount(), "coupon.amount.notnull", "couponAmount", context);
        valid &= this.validatorField.checkNotNull(value.getStock(), "coupon.stock.notnull", "stock", context);

        // --- DATE VALIDATION ---
        valid &= this.validatorField.checkNotNull(value.getStartDate(), "coupon.startDate.notnull", "startDate", context);
        valid &= this.validatorField.checkNotNull(value.getEndDate(), "coupon.endDate.notnull", "endDate", context);

        if(value.getStartDate() != null && value.getEndDate() != null && 
            value.getStartDate().isAfter(value.getEndDate())
        ){
            ValidatorField.addViolation(context, "coupon.startDate.before.endDate", "startDate");
            valid = false;
        }
        
        // --- TYPE PERCENT ---
        if(value.getType().equals(CouponEnum.PERCENT) && value.getCouponAmount() > 100){
            ValidatorField.addViolation(context, "coupon.type.percent.amount", "type");
            valid = false;
        }

        // --- UPDATE VALIDATION ---
        if (!value.isCreate()) {
            valid &= this.validatorField.checkNotNull(value.getId(), "coupon.id.notnull", "id", context);
        }
        return valid;
    }
}
