package vn.clothing.fashion_shop.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import vn.clothing.fashion_shop.constants.enumEntity.ApprovalMasterEnum;
import vn.clothing.fashion_shop.domain.ApprovalMaster;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ApprovalMasterResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;

public interface ApprovalMasterService {
    ApprovalMasterResponse createApprovalMaster(ApprovalMaster approvalMasterResponse);
    ApprovalMasterResponse updateApprovalMaster(ApprovalMaster approvalMasterResponse);
    ApprovalMasterResponse getApprovalMasterById(Long id);
    PaginationResponse getAllApprovalMaster(Pageable pageable, Specification spec);
    void deleteApprovalMasterById(Long id);
    ApprovalMaster findRawApprovalMasterById(Long id);
    ApprovalMaster findApprovalMasterByEntityStatusStep(String entityType, ApprovalMasterEnum status, Integer step, Long id);
    ApprovalMaster findRawApprovalMasterByEntityTypeAndStatus(String entityType, ApprovalMasterEnum status);
    List<ApprovalMaster> findRawAllApprovalMasterByEntityType(String entityType); 
}
