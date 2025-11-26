package vn.clothing.fashion_shop.web.validation.approval_master;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.util.ValidatorField;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ApprovalMasterRequest;

@Component
@RequiredArgsConstructor
public class ApprovalMasterMatchingValidator implements ConstraintValidator<ApprovalMasterMatching, ApprovalMasterRequest> {
    private final ValidatorField validatorField;
    @Override
    public boolean isValid(ApprovalMasterRequest value, ConstraintValidatorContext context) {
        boolean valid = true;
        if (value == null) {
            ValidatorField.addViolation(context, "approval.master.data.notnull", "ApprovalMasterRequest");
            return false;
        }

        // --- BASIC FIELDS ---
        valid &= this.validatorField.checkNotBlank(value.getEntityType(), "approval.master.entityType.notnull", "entityType", context);
        valid &= this.validatorField.checkNotNull(value.getStatus(), "approval.master.status.notnull", "status", context);
        valid &= this.validatorField.checkNotNull(value.getStep(), "approval.master.step.notnull", "step", context);

        if(!value.isCreate()){
            valid &= this.validatorField.checkNotNull(value.getId(), "approval.master.id.notnull", "id", context);
        }
        return valid;
    }
    
}
