package vn.clothing.fashion_shop.web.validation.permission;

import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.clothing.fashion_shop.web.rest.DTO.requests.PermissionRequest;

@Component
public class PermissionMatchingValidator implements ConstraintValidator<PermissionMatching, PermissionRequest> {

    @Override
    public boolean isValid(PermissionRequest value, ConstraintValidatorContext context) {
        boolean valid = true;
        List<String> methods = List.of("POST","PUT","PATCH","GET","DELETE");
        if(value.getApiPath() == null || value.getApiPath().trim().isEmpty()){
            addViolation(context,
            "permission.apipath.notnull"
            , "apiPath");
            valid = false;
        }
        if(value.isCreate()){
            if(value.getName() == null || value.getName().trim().isEmpty()){
                addViolation(context,
                "permission.name.notnull"
                , "name");
                valid = false;
            }
            if(value.getMethod() == null || value.getMethod().trim().isEmpty()){
                addViolation(context, 
                "permission.method.notnull"
                , "method");
                valid = false;
            }
            if(!methods.contains(value.getMethod().toUpperCase())){
                addViolation(context, "permission.method.notformat" , "method");
                valid = false;
            }
            if(value.getModule() == null || value.getModule().trim().isEmpty()){
                addViolation(context, "permission.module.notnull" , "module");
                valid = false;
            }
        }
        else{
            if(value.getId() == null){
                addViolation(context, "permission.id.notnull", "id");
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
