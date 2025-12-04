package vn.clothing.fashion_shop.web.validation.product;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.util.ValidatorField;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ProductRequest;
import vn.clothing.fashion_shop.web.rest.DTO.requests.VariantRequest.InnerVariantRequest;
@Component
@RequiredArgsConstructor
public class ProductMatchingValidator implements ConstraintValidator<ProductMatching, ProductRequest>  {
    private final ValidatorField validatorField;
    @Override
    public boolean isValid(ProductRequest value, ConstraintValidatorContext context) {
        boolean valid = true;
        if (value == null) {
            ValidatorField.addViolation(context, "shop.management.data.notnull", "ShopManagementRequest");
            return false;
        }
        // --- BASIC FIELDS ---
        valid &= this.validatorField.checkNotBlank(value.getName(), "product.name.notnull", "name", context);
        valid &= this.validatorField.checkNotNull(value.getPrice(), "product.price.notnull", "price", context);
        valid &= this.validatorField.checkNotNull(value.getCategory().getId(), "product.categoryId.notnull", "category", context);
        valid &= this.validatorField.checkNotNull(value.getShopManagement().getId(), "product.shopManagementId.notnull", "shopManagement", context);

        if (value.getVariants() != null && !value.getVariants().isEmpty()) {
            for (int i = 0; i < value.getVariants().size(); i++) {
                InnerVariantRequest variant = value.getVariants().get(i);

                // check skuId
                if (validatorField.checkNotBlank(variant.getSkuId(),
                        "product.variant.skuId.notnull",
                        "variants[" + i + "].skuId",
                        context) == false) {
                    valid = false;
                }

                // check optionValues
                if (variant.getOptionValues() == null || variant.getOptionValues().isEmpty()) {
                    ValidatorField.addViolation(context, 
                        "product.variant.optionValues.notnull",
                        "variants[" + i + "].optionValues");
                    valid = false;
                }

                // check price
                if (validatorField.checkNotNull(variant.getPrice(),
                        "product.variant.price.notnull",
                        "variants[" + i + "].price",
                        context) == false) {
                    valid = false;
                }

                // check stock
                if (validatorField.checkNotNull(variant.getStock(),
                        "product.variant.stock.notnull",
                        "variants[" + i + "].stock",
                        context) == false) {
                    valid = false;
                }
            }
        }
        if(!value.isCreate()){
            valid &= this.validatorField.checkNotNull(value.getId(), "product.id.notnull", "id", context);
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
