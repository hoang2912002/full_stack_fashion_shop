package vn.clothing.fashion_shop.web.validation.product;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.clothing.fashion_shop.web.rest.DTO.product.ValidationProductDTO;

public class ProductMatchingValidator implements ConstraintValidator<ProductMatching, ValidationProductDTO>  {

    @Override
    public boolean isValid(ValidationProductDTO value, ConstraintValidatorContext context) {
        boolean valid = true;
        if(value.getName().trim() == ""){
            addViolation(context, "Tên sản phẩm không được để trống", "name");
            valid = false;
        }
        
        if(value.getPrice() == null){
            addViolation(context, "Gía sản phẩm không được để trống", "price");
            valid = false;
        }
        // if(value.getPrice() instanceof Number){
        //     addViolation(context, "Gía sản phẩm không đúng định dạng", "price");
        //     valid = false;
        // }

        if(value.getQuantity() < 0){
            addViolation(context, "Số lượng tối thiểu là 0", "quantity");
            valid = false;
        }
        if(value.getCategory().getId() == null){
            addViolation(context, "Danh mục sản phẩm không được để trống", "category");
            valid = false;
        }
        // if(!value.getVariants().isEmpty()){

        // }
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
