package vn.clothing.fashion_shop.web.validation.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.clothing.fashion_shop.web.rest.DTO.user.ValidationUserDTO;
@Component
public class UserMatchingValidator implements ConstraintValidator<UserMatching, ValidationUserDTO> {
    
    @Override
    public boolean isValid(ValidationUserDTO value, ConstraintValidatorContext context) {
        boolean valid = true;

        if (value.getFullName() == null || value.getFullName().trim().isEmpty()) {
            addViolation(context, "user.fullname.notnull", "fullName");
            valid = false;
        }
        if (value.getAge() == null) {
            addViolation(context, "user.age.notnull", "age");
            valid = false;
        }
        if (value.isCreate()) {
            if (value.getEmail() == null || value.getEmail().isEmpty()) {
                addViolation(context, "user.email.notnull", "email");
                valid = false;
            } else {
                Pattern p = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
                Matcher matcher = p.matcher(value.getEmail());
                if (!matcher.matches()) {
                    addViolation(context, "user.email.notformat", "email");
                    valid = false;
                }
            }
            if (value.getPassword() == null || value.getPassword().isEmpty()) {
                addViolation(context, "user.password.notnull", "password");
                valid = false;
            } else {
                Pattern passwordPattern = Pattern.compile("^.{6,}$");
                Matcher matcherPassword = passwordPattern
                        .matcher(value.getPassword() != null ? value.getPassword() : "");

                if (!matcherPassword.matches()) {
                    addViolation(context, "user.password.limit", "password");
                    valid = false;
                }
            }
        } else {
            if (value.getId() == null) {
                addViolation(context, "user.id.notnull", "id");
                valid = false;
            }
        }

        return valid;
    }

    public static void addViolation(
            ConstraintValidatorContext context,
            String message,
            String property) {
        context.disableDefaultConstraintViolation();
        context
                .buildConstraintViolationWithTemplate(message)
                .addPropertyNode(property)
                .addConstraintViolation();
    }
}
