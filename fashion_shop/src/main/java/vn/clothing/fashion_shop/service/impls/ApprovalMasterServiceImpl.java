package vn.clothing.fashion_shop.service.impls;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.constants.enumEntity.ApprovalMasterEnum;
import vn.clothing.fashion_shop.constants.util.ConvertPagination;
import vn.clothing.fashion_shop.domain.ApprovalMaster;
import vn.clothing.fashion_shop.domain.Role;
import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.mapper.ApprovalMasterMapper;
import vn.clothing.fashion_shop.repository.ApprovalMasterRepository;
import vn.clothing.fashion_shop.service.ApprovalMasterService;
import vn.clothing.fashion_shop.service.RoleService;
import vn.clothing.fashion_shop.service.UserService;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ApprovalMasterResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.RoleResponse;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalMasterServiceImpl implements ApprovalMasterService {
    private final ApprovalMasterRepository approvalMasterRepository;
    private final ApprovalMasterMapper approvalMasterMapper;
    private final RoleService roleService;
    private final UserService userService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovalMasterResponse createApprovalMaster(ApprovalMaster approvalMaster) {
        log.info("[createApprovalMaster] Start create approval master");
        try {
            return saveOrUpdate(null, approvalMaster);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[createApprovalMaster] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    public ApprovalMasterResponse updateApprovalMaster(ApprovalMaster approvalMaster) {
        log.info("[updateApprovalMaster] Start update approval master");
        try {
            ApprovalMaster existingApprovalMaster = this.findRawApprovalMasterById(approvalMaster.getId());
            if(existingApprovalMaster == null){
                throw new ServiceException(
                    EnumError.APPROVAL_MASTER_ERR_NOT_FOUND_ID, 
                    "approval.master.not.found.id",
                    Map.of("id", approvalMaster.getId()));
            }
            return this.saveOrUpdate(existingApprovalMaster, approvalMaster);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[updateApprovalMaster] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    public ApprovalMasterResponse getApprovalMasterById(Long id) {
        try {
            ApprovalMaster approvalMaster = this.findRawApprovalMasterById(id);
            if(approvalMaster == null){
                throw new ServiceException(
                    EnumError.APPROVAL_MASTER_ERR_NOT_FOUND_ID, 
                    "approval.master.not.found.id",
                    Map.of("id", id));
            }
            return approvalMasterMapper.toDto(approvalMaster);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[getApprovalMasterById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    public PaginationResponse getAllApprovalMaster(Pageable pageable, Specification spec) {
        try {
            Page<ApprovalMaster> approvalMasters = this.approvalMasterRepository.findAll(spec, pageable);
            List<ApprovalMasterResponse> approvalMasterDTOs = approvalMasters.getContent().stream().map(r -> {
                return approvalMasterMapper.toDto(r);
            }).toList();
            return ConvertPagination.handleConvert(pageable, approvalMasters, approvalMasterDTOs);
        } catch (Exception e) {
            log.error("[getAllApprovalMaster] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    public void deleteApprovalMasterById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteApprovalMasterById'");
    }
    
    @Override
    @Transactional(readOnly = true)
    public ApprovalMaster findApprovalMasterByEntityStatusStep(String entityType, ApprovalMasterEnum status, Integer step, Long checkId) {
        try {
            Optional<ApprovalMaster> optionalApprovalMaster = checkId == null ?
                this.approvalMasterRepository.findByEntityTypeAndStatusAndStep(entityType, status, step) 
                : this.approvalMasterRepository.findByEntityTypeAndStatusAndStepAndIdNot(entityType, status, step, checkId);
            return optionalApprovalMaster.isPresent() ? optionalApprovalMaster.get() : null;
        } catch (Exception e) {
            log.error("[findApprovalMasterByEntityStatusStep] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApprovalMaster findRawApprovalMasterById(Long id) {
        try {
            Optional<ApprovalMaster> optionalApprovalMaster = this.approvalMasterRepository.findById(id);
            return optionalApprovalMaster.isPresent() ? optionalApprovalMaster.get() : null;
        } catch (Exception e) {
            log.error("[findRawApprovalMasterById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApprovalMaster> findRawAllApprovalMasterByEntityType(String entityType) {
        try {
            return this.approvalMasterRepository.findAllByEntityType(entityType);
        } catch (Exception e) {
            log.error("[findRawAllApprovalMasterByEntityType] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
    @Override
    @Transactional(readOnly = true)
    public ApprovalMaster findRawApprovalMasterByEntityTypeAndStatus(String entityType, ApprovalMasterEnum status) {
        try {
            Optional<ApprovalMaster> aOptional = this.approvalMasterRepository.findByEntityTypeAndStatus(entityType, status);
            return aOptional.isPresent() ? aOptional.get() : null;
        } catch (Exception e) {
            log.error("[findRawAllApprovalMasterByEntityType] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    private ApprovalMasterResponse saveOrUpdate(ApprovalMaster existing, ApprovalMaster approvalMaster) {
        try {
            validateUnique(approvalMaster, existing == null ? null : existing.getId());

            ApprovalMaster entity = (existing == null) ? new ApprovalMaster() : existing;

            entity.setEntityType(approvalMaster.getEntityType());
            entity.setStatus(approvalMaster.getStatus());
            entity.setStep(approvalMaster.getStep());
            entity.setRequired(approvalMaster.getRequired() == null ? false : approvalMaster.getRequired());

            if (approvalMaster.getRole() != null && approvalMaster.getRole().getId() != null) {
                Role role = roleService.getRawRoleById(approvalMaster.getRole().getId());
                entity.setRole(role);
                entity.setUser(null);
            } else if (approvalMaster.getUser() != null && approvalMaster.getUser().getId() != null ) {
                User user = userService.findRawUserById(approvalMaster.getUser().getId());
                entity.setUser(user);
                entity.setRole(user.getRole());
            } else {
                entity.setRole(null);
                entity.setUser(null);
            }

            // Save
            return approvalMasterMapper.toDto(approvalMasterRepository.save(entity));

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[ApprovalMaster] ERROR {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

     private void validateUnique(ApprovalMaster req, Long excludeId) {
        try {
            ApprovalMaster found = findApprovalMasterByEntityStatusStep(
                req.getEntityType(),
                req.getStatus(),
                req.getStep(),
                excludeId
            );
    
            if (found != null) {
                throw new ServiceException(
                    EnumError.APPROVAL_MASTER_DATA_EXISTED_ENTITY_TYPE_STATUS_STEP,
                    "approval.master.exist.entityType.status.step",
                    Map.of(
                        "entityType", req.getEntityType(),
                        "status", req.getStatus().name(),
                        "step", req.getStep()
                    )
                );
            }
            
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[ApprovalMaster] ERROR {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
}
