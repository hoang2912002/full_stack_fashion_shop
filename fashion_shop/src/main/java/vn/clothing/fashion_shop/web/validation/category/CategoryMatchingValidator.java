package vn.clothing.fashion_shop.web.validation.category;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.clothing.fashion_shop.web.rest.DTO.requests.CategoryRequest;

@Component
public class CategoryMatchingValidator implements ConstraintValidator<CategoryMatching, CategoryRequest>{

    @Override
    public boolean isValid(CategoryRequest value, ConstraintValidatorContext context) {
        boolean valid = true;

        if (value.getName() == null || value.getName().trim().isEmpty()) {
            addViolation(context, 
            "category.name.notnull"
            , "name");
            valid = false;
        }
        if(!value.isCreate()){
            if(value.getId() == null || value.getId() instanceof Long == false){
            addViolation(context, 
            "category.id.notnull"
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
