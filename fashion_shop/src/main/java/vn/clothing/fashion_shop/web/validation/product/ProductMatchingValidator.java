package vn.clothing.fashion_shop.web.validation.product;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.util.MessageUtil;
import vn.clothing.fashion_shop.web.rest.DTO.product.ValidationProductDTO;
@Component
@RequiredArgsConstructor
public class ProductMatchingValidator implements ConstraintValidator<ProductMatching, ValidationProductDTO>  {
    private final MessageUtil messageUtil;
    @Override
    public boolean isValid(ValidationProductDTO value, ConstraintValidatorContext context) {
        boolean valid = true;
        if(value.getName() == null || value.getName().trim().isEmpty()){
            addViolation(context, messageUtil.getMessage("product.name.notnull"), "name");
            valid = false;
        }
        
        if(value.getPrice() == null){
            addViolation(context, messageUtil.getMessage("product.price.notnull"), "price");
            valid = false;
        }
        // if(value.getPrice() instanceof Number){
        //     addViolation(context, "Gía sản phẩm không đúng định dạng", "price");
        //     valid = false;
        // }

        if(value.getQuantity() < 0){
            addViolation(context, messageUtil.getMessage("product.quantity.limit"), "quantity");
            valid = false;
        }
        if(value.getCategory().getId() == null){
            addViolation(context, messageUtil.getMessage("product.categoryid.notnull"), "category");
            valid = false;
        }
        // if(!value.getVariants().isEmpty()){

        // }
        if(!value.isCreate()){
            if(value.getId() == null && value.getId() instanceof Long == false){
                addViolation(context, messageUtil.getMessage("product.id.notnull"), "id");
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
