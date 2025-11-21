package vn.clothing.fashion_shop.service.impls;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.domain.ApprovalHistory;
import vn.clothing.fashion_shop.domain.ApprovalMaster;
import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.mapper.ApprovalHistoryMapper;
import vn.clothing.fashion_shop.repository.ApprovalHistoryRepository;
import vn.clothing.fashion_shop.security.SecurityUtils;
import vn.clothing.fashion_shop.service.ApprovalHistoryService;
import vn.clothing.fashion_shop.service.ApprovalMasterService;
import vn.clothing.fashion_shop.service.UserService;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ApprovalHistoryResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalHistoryServiceImpl implements ApprovalHistoryService {
    private final ApprovalMasterService approvalMasterService;
    private final ApprovalHistoryRepository approvalHistoryRepository;
    private final ApprovalHistoryMapper approvalHistoryMapper;
    private final UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovalHistoryResponse createApprovalHistory(ApprovalHistory approvalHistory, boolean skipCheckPeriodDataExist) {
        log.info("[createApprovalHistory] Start create approval history");
        try {
            String email = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
            ApprovalMaster approvalMaster = this.approvalMasterService.findRawApprovalMasterById(approvalHistory.getApprovalMaster().getId());
            User user = this.userService.handleGetUserByEmail(email);
            //Kiểm tra tồn tại của Approval Master
            if(approvalMaster == null){
                throw new ServiceException(
                    EnumError.APPROVAL_MASTER_ERR_NOT_FOUND_ID,
                    "approvalMaster.id.notFound"
                );
            }
            //Kiểm tra quyền của user
            if(user.getRole().getId() != approvalMaster.getRole().getId()){
                throw new ServiceException(
                    EnumError.PERMISSION_ACCESS_DENIED,
                    "You do not have permission to access this resource"
                );
            }
            if(skipCheckPeriodDataExist == false){
                List<ApprovalHistory> existingHistories = this.findRawAllApprovalHistoryByApprovalMasterId(approvalHistory.getApprovalMaster().getId());
                ApprovalHistory lastHistory = existingHistories.isEmpty() ? null : existingHistories.get(existingHistories.size() - 1);
                String status = null;
                if (lastHistory != null && lastHistory.getApprovalMaster() != null && lastHistory.getApprovalMaster().getStatus() != null) {
                    status = lastHistory.getApprovalMaster().getStatus().name();
                }
            }
            return approvalHistoryMapper.toDto(this.approvalHistoryRepository.save(approvalHistory));            
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[createApprovalHistory] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }  
    }

    @Override
    public ApprovalHistoryResponse updateApprovalHistory(ApprovalHistory approvalHistory) {
        log.info("[updateApprovalHistory] Start update approval history");
        try {
            return null;            
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[updateApprovalHistory] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    public ApprovalHistoryResponse getApprovalHistoryById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getApprovalHistoryById'");
    }

    @Override
    public PaginationResponse getAllApprovalHistories(Pageable pageable, Specification spec) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllApprovalHistories'");
    }

    @Override
    public void deleteApprovalHistory(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteApprovalHistory'");
    }
    
    @Override
    public ApprovalHistory findRawApprovalHistoryById(Long id) {
        try {
            Optional<ApprovalHistory> approvalHistoryOpt = this.approvalHistoryRepository.findById(id);
            return approvalHistoryOpt.isPresent() ? approvalHistoryOpt.get() : null;
        } catch (Exception e) {
            log.error("[findRawApprovalHistoryById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApprovalHistory> findRawAllApprovalHistoryByApprovalMasterIdsAndRequestId(List<Long> approvalMasterIds, Long requestId, boolean lastApprovalHistoryOnly) {
        try {
            if (lastApprovalHistoryOnly) {
                ApprovalHistory last = this.approvalHistoryRepository.findFirstByApprovalMasterIdInAndRequestIdOrderByApprovedAtDesc(approvalMasterIds, requestId);
                return last != null ? List.of(last) : List.of();
            } else {
                return this.approvalHistoryRepository.findAllByApprovalMasterIdInAndRequestId(approvalMasterIds, requestId);
            }
        } catch (Exception e) {
            log.error("[findRawAllApprovalHistoryByApprovalMasterIdsAndRequestId] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApprovalHistory> findRawAllApprovalHistoryByApprovalMasterId(Long approvalMasterId) {
        try {
            return this.approvalHistoryRepository.findAllByApprovalMasterIdOrderByApprovedAtAsc(approvalMasterId);
        } catch (Exception e) {
            log.error("[findRawAllApprovalHistoryByApprovalMasterId] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
}
