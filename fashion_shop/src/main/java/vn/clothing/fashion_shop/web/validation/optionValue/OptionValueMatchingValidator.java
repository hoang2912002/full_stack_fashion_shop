package vn.clothing.fashion_shop.web.validation.optionValue;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.clothing.fashion_shop.web.rest.DTO.optionValue.ValidationOptionValueDTO;

public class OptionValueMatchingValidator implements ConstraintValidator<OptionValueMatching, ValidationOptionValueDTO> {
    @Override
    public boolean isValid(ValidationOptionValueDTO value, ConstraintValidatorContext context) {
        boolean valid = true;
        if(value.getValue().trim() == ""){
            addViolation(context, "Gía trị option không được để trống", "value");
            valid = false;
        }
        return valid;
    }

    public static void addViolation(
        ConstraintValidatorContext context,
        String message,
        String property
    ) {
        context.disableDefaultConstraintViolation();
        context
            .buildConstraintViolationWithTemplate(message)
            .addPropertyNode(property)
            .addConstraintViolation();
    }

    
}
