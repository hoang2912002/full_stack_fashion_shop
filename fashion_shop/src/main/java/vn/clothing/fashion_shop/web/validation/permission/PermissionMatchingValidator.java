package vn.clothing.fashion_shop.web.validation.permission;

import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.clothing.fashion_shop.domain.Permission;
import vn.clothing.fashion_shop.web.rest.DTO.permission.ValidationPermissionDTO;

public class PermissionMatchingValidator implements ConstraintValidator<PermissionMatching, ValidationPermissionDTO> {

    @Override
    public boolean isValid(ValidationPermissionDTO value, ConstraintValidatorContext context) {
        boolean valid = true;
        List<String> methods = List.of("POST","PUT","PATCH","GET","DELETE");
        if(value.getApiPath().trim() == ""){
            addViolation(context, "ApiPath không được để trống", "apiPath");
            valid = false;
        }
        if(value.isCreate()){
            if(value.getName().trim() == ""){
                addViolation(context, "Tên permission không được để trống", "name");
                valid = false;
            }
            if(value.getMethod().trim() == ""){
                addViolation(context, "Method không được để trống", "method");
                valid = false;
            }
            if(!methods.contains(value.getMethod().toUpperCase())){
                addViolation(context, "Method không đúng định dạng", "method");
                valid = false;
            }
            if(value.getModule().trim() == ""){
                addViolation(context, "Module không được để trống", "module");
                valid = false;
            }
        }
        else{
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
