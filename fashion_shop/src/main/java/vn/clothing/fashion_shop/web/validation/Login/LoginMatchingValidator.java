package vn.clothing.fashion_shop.web.validation.Login;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.clothing.fashion_shop.web.rest.DTO.authenticate.LoginDTO;

public class LoginMatchingValidator  implements ConstraintValidator<LoginMatching, LoginDTO> {

    @Override
    public boolean isValid(LoginDTO value, ConstraintValidatorContext context) {
        boolean valid = true;
        Pattern p = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
        Matcher matcher = p.matcher(value.getUsername());

        Pattern passwordPattern = Pattern.compile("^.{6,}$");
        Matcher matcherPassword = passwordPattern.matcher(value.getPassword());


        if(value.getUsername().trim() == ""){
            addViolation(context, "Username không được bỏ trống", "username");
            valid = false;
        }
        if(!matcher.matches()){
            addViolation(context, "Username không đúng định dạng", "username");
            valid = false;
        }
        if(value.getPassword().isBlank()){
            addViolation(context, "Mật khẩu không được bỏ trống", "password");
            valid = false;
        }
        if(!matcherPassword.matches()){
            addViolation(context, "Mật khẩu bao gồm ít nhất 6 ký tự", "password");
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
