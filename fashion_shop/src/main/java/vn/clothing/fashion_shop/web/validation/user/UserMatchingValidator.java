package vn.clothing.fashion_shop.web.validation.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.clothing.fashion_shop.web.rest.DTO.user.ValidationUserDTO;

public class UserMatchingValidator implements ConstraintValidator<UserMatching, ValidationUserDTO> {

    @Override
    public boolean isValid(ValidationUserDTO value, ConstraintValidatorContext context) {
        boolean valid = true;

        if (value.getFullName().trim() == "") {
            addViolation(context, "Tên không được để trống", "fullName");
            valid = false;
        }
        if (value.getAge() == null) {
            addViolation(context, "Tuổi không được bỏ trống", "age");
            valid = false;
        }
        if (value.isCreate()) {
            if (value.getEmail() == null || value.getEmail().isEmpty()) {
                addViolation(context, "Email không được để trống", "email");
                valid = false;
            } else {
                Pattern p = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
                Matcher matcher = p.matcher(value.getEmail());
                if (!matcher.matches()) {
                    addViolation(context, "Email không đúng định dạng. Ví dụ: a@gmail.com", "email");
                    valid = false;
                }
            }
            if (value.getPassword() == null || value.getPassword().isEmpty()) {
                addViolation(context, "Mật khẩu không được bỏ trống", "password");
                valid = false;
            } else {
                Pattern passwordPattern = Pattern.compile("^.{6,}$");
                Matcher matcherPassword = passwordPattern
                        .matcher(value.getPassword() != null ? value.getPassword() : "");

                if (!matcherPassword.matches()) {
                    addViolation(context, "Mật khẩu bao gồm ít nhất 6 ký tự", "password");
                    valid = false;
                }
            }
        } else {
            if (value.getId() == null) {
                addViolation(context, "Id không được bỏ trống", "id");
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
