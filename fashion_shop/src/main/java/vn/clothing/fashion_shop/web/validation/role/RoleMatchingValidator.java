package vn.clothing.fashion_shop.web.validation.role;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.clothing.fashion_shop.web.rest.DTO.role.ValidationRoleDTO;

public class RoleMatchingValidator implements ConstraintValidator<RoleMatching, ValidationRoleDTO> {

    @Override
    public boolean isValid(ValidationRoleDTO value, ConstraintValidatorContext context) {
        boolean valid = true;
        if(value.getName().trim() == ""){
            addViolation(context, "Tên permission không được để trống", "name");
            valid = false;
        }

        if(!value.isCreate()){
            if(value.getId() == null && value.getId() instanceof Long == false){
                addViolation(context, "Id không được để trống", "id");
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
