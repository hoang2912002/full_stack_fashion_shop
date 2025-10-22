package vn.clothing.fashion_shop.web.validation.category;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.clothing.fashion_shop.web.rest.DTO.category.ValidationCategoryDTO;

public class CategoryMatchingValidator implements ConstraintValidator<CategoryMatching, ValidationCategoryDTO>{

    @Override
    public boolean isValid(ValidationCategoryDTO value, ConstraintValidatorContext context) {
        boolean valid = true;
        if(value.getName().trim() == ""){
            addViolation(context, "Tên danh mục không được để trống", "name");
            valid = false;
        }
        if(!value.isCreate()){
            if(value.getId() == null || value.getId() instanceof Long == false){
            addViolation(context, "Id danh mục không được để trống", "id");
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
