package vn.clothing.fashion_shop.web.validation.Login;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.util.MessageUtil;
import vn.clothing.fashion_shop.web.rest.DTO.authenticate.LoginDTO;
@Component
@RequiredArgsConstructor
public class LoginMatchingValidator  implements ConstraintValidator<LoginMatching, LoginDTO> {
    private final MessageUtil messageUtil;
    @Override
    public boolean isValid(LoginDTO value, ConstraintValidatorContext context) {
        boolean valid = true;
        Pattern p = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
        Matcher matcher = p.matcher(value.getUsername());

        Pattern passwordPattern = Pattern.compile("^.{6,}$");
        Matcher matcherPassword = passwordPattern.matcher(value.getPassword());


        if(value.getUsername() == null || value.getUsername().trim().isEmpty()){
            addViolation(context, 
            messageUtil.getMessage("user.email.notnull")
            , "username");
            valid = false;
        }
        if(!matcher.matches()){
            addViolation(context, 
            messageUtil.getMessage("user.email.notformat")
            , "username");
            valid = false;
        }
        if(value.getPassword() == null || value.getPassword().trim().isEmpty()){
            addViolation(context, 
            messageUtil.getMessage("user.password.notnull")
            , "password");
            valid = false;
        }
        if(!matcherPassword.matches()){
            addViolation(context, 
            messageUtil.getMessage("user.password.limit")
            , "password");
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
