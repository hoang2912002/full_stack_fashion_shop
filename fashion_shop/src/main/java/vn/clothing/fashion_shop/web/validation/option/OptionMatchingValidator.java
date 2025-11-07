package vn.clothing.fashion_shop.web.validation.option;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.clothing.fashion_shop.web.rest.DTO.option.ValidationOptionDTO;
@Component
public class OptionMatchingValidator implements ConstraintValidator<OptionMatching, ValidationOptionDTO>  {

    @Override
    public boolean isValid(ValidationOptionDTO value, ConstraintValidatorContext context) {
        boolean valid = true;
        if(value.getName().trim() == ""){
            addViolation(context, 
            "option.name.notnull"
            , "name");
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
