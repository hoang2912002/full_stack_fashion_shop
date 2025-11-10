package vn.clothing.fashion_shop.web.validation.optionValue;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.clothing.fashion_shop.web.rest.DTO.requests.OptionValueRequest;
@Component
public class OptionValueMatchingValidator implements ConstraintValidator<OptionValueMatching, OptionValueRequest> {
    @Override
    public boolean isValid(OptionValueRequest value, ConstraintValidatorContext context) {
        boolean valid = true;
        if(value.getValue() == null || value.getValue().trim().isEmpty()){
            addViolation(context, 
            "option.value.value.notnull"
            , "value");
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
