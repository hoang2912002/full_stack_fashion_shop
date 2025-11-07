package vn.clothing.fashion_shop.web.validation.category;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.util.MessageUtil;
import vn.clothing.fashion_shop.web.rest.DTO.category.ValidationCategoryDTO;

@Component
@RequiredArgsConstructor
public class CategoryMatchingValidator implements ConstraintValidator<CategoryMatching, ValidationCategoryDTO>{
    private final MessageUtil messageUtil;

    @Override
    public boolean isValid(ValidationCategoryDTO value, ConstraintValidatorContext context) {
        boolean valid = true;

        if (value.getName() == null || value.getName().trim().isEmpty()) {
            addViolation(context, 
            messageUtil.getMessage("category.name.notnull")
            , "name");
            valid = false;
        }
        if(!value.isCreate()){
            if(value.getId() == null || value.getId() instanceof Long == false){
            addViolation(context, 
            messageUtil.getMessage("category.id.notnull")
            , "id");
            valid = false;
        }
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
