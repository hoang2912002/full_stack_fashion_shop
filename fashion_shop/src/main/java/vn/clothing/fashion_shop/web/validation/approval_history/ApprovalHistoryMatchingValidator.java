package vn.clothing.fashion_shop.web.validation.approval_history;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.util.ValidatorField;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ApprovalHistoryRequest;

@Component
@RequiredArgsConstructor
public class ApprovalHistoryMatchingValidator implements ConstraintValidator<ApprovalHistoryMatching, ApprovalHistoryRequest> {
    private final ValidatorField validatorField;

    @Override
    public boolean isValid(ApprovalHistoryRequest value, ConstraintValidatorContext context) {
        boolean valid = true;
        if (value == null) {
            ValidatorField.addViolation(context, "approval.history.data.notnull", "ApprovalHistoryRequest");
            return false;
        }
        // --- BASIC FIELDS ---
        valid &= this.validatorField.checkNotBlank(value.getEntityType(), "approval.master.entityType.notnull", "entityType", context);
        valid &= this.validatorField.checkNotNull(value.getApprovalMaster().getId(), "approval.history.approvalMaster.notnull", "approval master", context);
        valid &= this.validatorField.checkNotNull(value.getRequestId(), "approval.history.exist.requestId", "request id", context);

        if(!value.isCreate()){
            valid &= this.validatorField.checkNotNull(value.getId(), "approval.history.id.notnull", "id", context);
        }
        return valid;
    }
}
