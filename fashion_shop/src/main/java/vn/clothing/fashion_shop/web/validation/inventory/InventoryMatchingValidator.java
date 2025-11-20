package vn.clothing.fashion_shop.web.validation.inventory;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.util.ValidatorField;
import vn.clothing.fashion_shop.web.rest.DTO.requests.InventoryRequest;

@Component
@RequiredArgsConstructor
public class InventoryMatchingValidator implements ConstraintValidator<InventoryMatching, InventoryRequest> {
    private final ValidatorField validatorField;
    @Override
    public boolean isValid(InventoryRequest value, ConstraintValidatorContext context) {
        boolean valid = true;
        if (value == null) {
            ValidatorField.addViolation(context, "promotion.data.notnull", "PromotionRequest");
            return false;
        }

        // --- BASIC FIELDS ---
        valid &= this.validatorField.checkNotNull(value.getQuantityAvailable(), "inventory.quantityAvailable.notnull", "quantityAvailable", context);
        valid &= this.validatorField.checkNotNull(value.getQuantityReserved(), "inventory.quantityReserved.notnull", "quantityReserved", context);
        valid &= this.validatorField.checkNotNull(value.getQuantitySold(), "inventory.quantitySold.notnull", "quantitySold", context);

        if(!value.isCreate()){
            valid &= this.validatorField.checkNotNull(value.getId(), "inventory.id.notnull", "id", context);
        }
        return valid;
    }
    
}
