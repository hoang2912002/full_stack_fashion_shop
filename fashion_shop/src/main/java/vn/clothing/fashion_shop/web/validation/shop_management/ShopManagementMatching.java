package vn.clothing.fashion_shop.web.validation.shop_management;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ShopManagementMatchingValidator.class)
@Target({ ElementType.TYPE,ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShopManagementMatching {
    String message() default "Field này không được để trống!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
