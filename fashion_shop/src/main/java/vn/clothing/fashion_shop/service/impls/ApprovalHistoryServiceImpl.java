package vn.clothing.fashion_shop.service.impls;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.constants.enumEntity.ApprovalMasterEnum;
import vn.clothing.fashion_shop.constants.util.FormatTime;
import vn.clothing.fashion_shop.domain.ApprovalHistory;
import vn.clothing.fashion_shop.domain.ApprovalMaster;
import vn.clothing.fashion_shop.domain.Inventory;
import vn.clothing.fashion_shop.domain.Product;
import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.mapper.ApprovalHistoryMapper;
import vn.clothing.fashion_shop.repository.ApprovalHistoryRepository;
import vn.clothing.fashion_shop.repository.ProductRepository;
import vn.clothing.fashion_shop.security.SecurityUtils;
import vn.clothing.fashion_shop.service.ApprovalHistoryService;
import vn.clothing.fashion_shop.service.ApprovalMasterService;
import vn.clothing.fashion_shop.service.InventoryService;
import vn.clothing.fashion_shop.service.ProductService;
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
    private final InventoryService inventoryService;
    private final UserService userService;
    private final ProductRepository productRepository;
    public final static String ENTITY_TYPE_PRODUCT = "PRODUCT";
    public final static String ENTITY_TYPE_INVENTORY = "INVENTORY";
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovalHistoryResponse createApprovalHistory(
        ApprovalHistory approvalHistory,
        boolean skipCheckPeriodDataExist,
        String entityType) 
    {
        log.info("[createApprovalHistory] Start create approval history");

        try {
            User user = this.userService.getCurrentUser();
            ApprovalMaster approvalMaster = getApprovalMaster(approvalHistory, entityType);

            validateUserPermission(user, approvalMaster);

            if (!skipCheckPeriodDataExist) {
                handleApprovalBusinessRules(approvalHistory, approvalMaster);
            }
            Instant approvedAt = Instant.now();
            approvalHistory.setNote(String.format("Create %s - create existing approval request in %s at: %s",
                entityType.toLowerCase(),
                approvalMaster.getStatus().toString(),
                FormatTime.formatDateTime(approvedAt)
            ));
            approvalHistory.setApprovedAt(approvedAt);
            // Save chính history user gửi lên
            ApprovalHistory saved = approvalHistoryRepository.save(approvalHistory);
            return approvalHistoryMapper.toDto(saved);

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[createApprovalHistory] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
    @Override
    public ApprovalHistoryResponse updateApprovalHistory(ApprovalHistory approvalHistory, String entityType) {
        log.info("[updateApprovalHistory] Start update approval history");
        try {
            String email = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
            User user = this.userService.handleGetUserByEmail(email);
            if (user == null) {
                throw new ServiceException(
                    EnumError.USER_ERR_NOT_FOUND_EMAIL,
                    "user.account.not.found"
                );
            }
            ApprovalMaster approvalMaster = this.approvalMasterService.findRawApprovalMasterById(approvalHistory.getApprovalMaster().getId());
            ApprovalHistory updateApprovalHistory = this.findRawApprovalHistoryById(approvalHistory.getId());
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
                    "permission.access.deny"
                );
            }
            if(updateApprovalHistory == null){
                throw new ServiceException(
                    EnumError.APPROVAL_HISTORY_ERR_NOT_FOUND_ID,
                    "approval.history.not.found.id",
                    Map.of("id", approvalHistory.getId())
                );
            }
            return approvalHistoryMapper.toDto(this.approvalHistoryRepository.save(approvalHistory));            
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
    
    @Override
    @Transactional(readOnly = true)
    public List<ApprovalHistory> findRawAllApprovalHistoryByRequestId(Long requestId) {
        try {
            return this.approvalHistoryRepository.findAllByRequestIdOrderByApprovedAtAsc(requestId);
        } catch (Exception e) {
            log.error("[findRawAllApprovalHistoryByRequestId] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void handleApprovalHistoryUpSertProduct(Product product, Long productId, String entityType) 
    {
        try {
            List<ApprovalMaster> approvalMasters = this.approvalMasterService.findRawAllApprovalMasterByEntityType(ENTITY_TYPE_PRODUCT);
            
            List<Long> approvalMasterIds = approvalMasters.stream().map(ApprovalMaster::getId).toList();
            
            Map<ApprovalMasterEnum, ApprovalMaster> productApprovals = approvalMasters.stream()
                .filter(a -> ENTITY_TYPE_PRODUCT.equalsIgnoreCase(a.getEntityType()))
                .collect(Collectors.toMap(ApprovalMaster::getStatus, Function.identity(), (a, b) -> a));

            //Kiểm tra tồn kho
            List<Inventory> existingInventories = this.inventoryService.findRawInventoriesByProductId(product.getId());
            
            ApprovalHistory approvalHistoryToCreate = null;
            ApprovalHistory approvalHistoryToUpdate = null;
            if(productId == null){
                List<ApprovalHistory> approvalHistories = this.findRawAllApprovalHistoryByApprovalMasterIdsAndRequestId(approvalMasterIds, product.getId(), true);
                
                //Kiểm tra trạng thái phê duyệt của sản phẩm
                if (!approvalHistories.isEmpty()) {
                    ApprovalHistory lastApprovalHistory = approvalHistories.get(approvalHistories.size() - 1);
                    ApprovalMasterEnum status = lastApprovalHistory.getApprovalMaster().getStatus();
                    if (status == ApprovalMasterEnum.PENDING) {
                        throw new ServiceException(EnumError.PRODUCT_DATA_EXISTED_APPROVAL_PENDING, "product.data.existed.approval.pending", Map.of("name", product.getName()));
                    } else if (status == ApprovalMasterEnum.APPROVED) {
                        throw new ServiceException(EnumError.PRODUCT_DATA_EXISTED_NAME, "product.data.duplicate.approval.approved", Map.of("name", product.getName()));
                    } else if (status == ApprovalMasterEnum.REJECTED) {
                        throw new ServiceException(EnumError.APPROVAL_MASTER_DATA_STATUS_REJECTED_CANNOT_ADD_HISTORY, "approval.master.data.status.rejected.cannot.add.history", Map.of("name", product.getName()));
                    } else {
                        throw new ServiceException(EnumError.PRODUCT_DATA_EXISTED_NAME, "approval.history.exist.requestId", Map.of("name", product.getName()));
                    }
                }
                Integer existingInventory = existingInventories != null ? existingInventories.size() : 0;
                if (existingInventory != null && existingInventory > 0) {
                    throw new ServiceException(EnumError.INVENTORY_DATA_EXISTED_PRODUCT_ID, "inventory.exist.product.id", Map.of("ID", product.getId()));
                }
                if (productApprovals.get(ApprovalMasterEnum.PENDING) == null) {
                    throw new ServiceException(EnumError.APPROVAL_MASTER_ERR_NOT_FOUND_ENTITY_TYPE_STATUS, "approval.master.not.found.entityType.status", Map.of("entityType", "PRODUCT"));
                }
                //Tạo mới quy trình phê duyệt với trạng thái PENDING cho sản phẩm
                approvalHistoryToCreate =
                    ApprovalHistory.builder()
                    .approvalMaster(productApprovals.get(ApprovalMasterEnum.PENDING))
                    .requestId(product.getId())
                    .approvedAt(Instant.now())
                    .build();
            }
            else{
                List<ApprovalHistory> approvalHistories = this.findRawAllApprovalHistoryByApprovalMasterIdsAndRequestId(approvalMasterIds, product.getId(), true);
                // Không có history -> tạo request PENDING
                if (approvalHistories.isEmpty()) {
                    ApprovalMaster pendingMaster = productApprovals.get(ApprovalMasterEnum.PENDING);
                    if (pendingMaster == null) {
                        throw new ServiceException(EnumError.APPROVAL_MASTER_ERR_NOT_FOUND_ENTITY_TYPE_STATUS, "approval.master.not.found.entityType.status", Map.of("entityType", "PRODUCT"));
                    }
                    approvalHistoryToCreate = ApprovalHistory.builder()
                        .approvalMaster(pendingMaster)
                        .requestId(product.getId())
                        .approvedAt(Instant.now())
                        .note("Update old product - create new approval request")
                        .build();
                } else {
                    ApprovalMasterEnum lastStatus = approvalHistories.get(0).getApprovalMaster().getStatus();
                    switch (lastStatus) {
                        case PENDING:
                            // Đang chờ duyệt -> tiếp tục xử lý, không tạo history mới
                            approvalHistoryToUpdate = ApprovalHistory.builder()
                                .id(approvalHistories.get(0).getId())
                                .approvalMaster(approvalHistories.get(0).getApprovalMaster())
                                .requestId(product.getId())
                                .approvedAt(Instant.now())
                                .note("Update product - update existing approval request in PENDING at: " + FormatTime.formatDateTime(Instant.now()))
                                .build();
                            break;
                        case ADJUSTMENT:
                            // Đang ở trạng thái điều chỉnh -> không tạo history mới, tiếp tục xử lý điều chỉnh
                            approvalHistoryToUpdate = ApprovalHistory.builder()
                                .id(approvalHistories.get(0).getId())
                                .approvalMaster(productApprovals.get(ApprovalMasterEnum.FINISHED_ADJUSTMENT))
                                .requestId(product.getId())
                                .approvedAt(Instant.now())
                                .note("Update product - update existing approval request in FINISHED_ADJUSTMENT at: " + FormatTime.formatDateTime(Instant.now()))
                                .build();
                            break;
                        case APPROVED:
                            // Đã phê duyệt -> mặc định không cho chỉnh sửa
                            throw new ServiceException(EnumError.INVENTORY_ERR_NOT_UPDATE_STATUS_APPROVED, "product.not.update.approval.status.approved", Map.of("ID", product.getId()));
                        case NEEDS_ADJUSTMENT:
                            throw new ServiceException(EnumError.INVENTORY_ERR_NOT_UPDATE_STATUS_APPROVED, "product.not.update.approval.status.needsAdjustment", Map.of("ID", product.getId()));
                        
                        case FINISHED_ADJUSTMENT:
                            throw new ServiceException(EnumError.INVENTORY_ERR_NOT_UPDATE_STATUS_APPROVED, "product.not.update.approval.status.finishedAdjustment", Map.of("ID", product.getId()));
                        
                        case REJECTED:
                            throw new ServiceException(EnumError.INVENTORY_ERR_NOT_UPDATE_STATUS_APPROVED, "product.not.update.approval.status.rejected", Map.of("ID", product.getId()));
                        default:
                            log.info("[upSertProduct] productId {} has approval status {}, continue", product.getId(), lastStatus);
                            break;
                    }
                }
            }
            if (approvalHistoryToCreate != null) {
                this.createApprovalHistory(approvalHistoryToCreate, true, entityType);
            }
            if (approvalHistoryToUpdate != null) {
                this.updateApprovalHistory(approvalHistoryToUpdate, entityType);
            }
        } catch(ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[approvalHistoryProduct] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }


    private ApprovalMaster getApprovalMaster(ApprovalHistory approvalHistory, String entityType) {
        try {
            Map<Long, ApprovalMaster> masterMap = approvalMasterService
                .findRawAllApprovalMasterByEntityType(entityType)
                .stream()
                .collect(Collectors.toMap(
                        ApprovalMaster::getId,
                        Function.identity(),
                        (a, b) -> a
                ));
    
            ApprovalMaster master = masterMap.get(
                    approvalHistory.getApprovalMaster().getId()
            );
    
            if (master == null) {
                throw new ServiceException(
                        EnumError.APPROVAL_MASTER_ERR_NOT_FOUND_ID,
                        "approvalMaster.id.notFound"
                );
            }
    
            return master;
        } catch (Exception e) {
            log.error("[getApprovalMaster] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    private void validateUserPermission(User user, ApprovalMaster approvalMaster) {
        if (!Objects.equals(user.getRole().getId(), approvalMaster.getRole().getId())) {
            throw new ServiceException(
                    EnumError.PERMISSION_ACCESS_DENIED,
                    "permission.access.deny"
            );
        }
    }

    private void handleApprovalBusinessRules(ApprovalHistory approvalHistory, ApprovalMaster master) {
        try {
            ApprovalMasterEnum status = master.getStatus();
            Long requestId = approvalHistory.getRequestId();
    
            List<ApprovalHistory> historyList =
                    findRawAllApprovalHistoryByRequestId(requestId);
            ApprovalHistory last = historyList.isEmpty() ? null : historyList.getLast();
            boolean a=  master.getEntityType().equals(ENTITY_TYPE_PRODUCT);
            switch (master.getEntityType()) {
                case ENTITY_TYPE_PRODUCT -> handleProductApproval(status, last, requestId);
                case ENTITY_TYPE_INVENTORY -> handleInventoryApproval(requestId);
                default -> throw new ServiceException(
                        EnumError.INTERNAL_ERROR,
                        "entityType.not.supported"
                );
            }
            
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[handleApprovalBusinessRules] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    private void handleProductApproval(
        ApprovalMasterEnum status,
        ApprovalHistory lastHistory,
        Long productId) 
    {
        try {
            Product product = productRepository.findById(productId).orElse(null);
    
            if (product == null) {
                throw new ServiceException(
                        EnumError.PRODUCT_ERR_NOT_FOUND_ID,
                        "product.not.found.id",
                        Map.of("id", productId)
                );
            }
    
            if (lastHistory == null) {
                if (!status.equals(ApprovalMasterEnum.PENDING)) {
                    throw new ServiceException(
                            EnumError.APPROVAL_MASTER_DATA_STATUS_REJECTED_CANNOT_ADD_HISTORY,
                            "approval.history.current.not.pending",
                            Map.of("name", product.getName())
                    );
                }
                return; // ok
            }
    
            ApprovalMasterEnum lastStatus = lastHistory.getApprovalMaster().getStatus();
    
            switch (lastStatus) {
                case PENDING -> {
                    if (status != ApprovalMasterEnum.APPROVED) {
                        throw new ServiceException(
                            EnumError.PRODUCT_DATA_EXISTED_APPROVAL_PENDING,
                            "product.data.existed.approval.pending",
                            Map.of("name", product.getName())
                        );
                    }
                }
                case APPROVED -> {
                    if (status != ApprovalMasterEnum.NEEDS_ADJUSTMENT) {
                        throw new ServiceException(
                            EnumError.PRODUCT_DATA_EXISTED_NAME,
                            "approval.history.last.approved.current.not.needsAdjustment",
                            Map.of("name", product.getName())
                        );
                    }
                }
                case REJECTED -> {
                    if (status != ApprovalMasterEnum.NEEDS_ADJUSTMENT) {
                        throw new ServiceException(
                            EnumError.APPROVAL_MASTER_DATA_STATUS_REJECTED_CANNOT_ADD_HISTORY,
                            "approval.history.last.rejected.current.not.needsAdjustment",
                            Map.of("name", product.getName())
                        );
                    }
                }
                case NEEDS_ADJUSTMENT -> {
                    if (status != ApprovalMasterEnum.FINISHED_ADJUSTMENT) {
                        throw new ServiceException(
                            EnumError.APPROVAL_MASTER_DATA_STATUS_REJECTED_CANNOT_ADD_HISTORY,
                            "approval.history.last.needsAdjustment.current.not.finishedAdjustment",
                            Map.of("name", product.getName())
                        );
                    }
                }
                case FINISHED_ADJUSTMENT -> {
                    if (status != ApprovalMasterEnum.APPROVED) {
                        throw new ServiceException(
                            EnumError.APPROVAL_MASTER_DATA_STATUS_REJECTED_CANNOT_ADD_HISTORY,
                            "approval.history.last.finishedAdjustment.current.not.approved",
                            Map.of("name", product.getName())
                        );
                    }
                }
                default -> throw new ServiceException(
                    EnumError.INTERNAL_ERROR,
                    "approval.history.status.invalid.flow"
                );
            }
            
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[handleProductApproval] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    private void handleInventoryApproval(Long productId) {
        List<Inventory> inventories = inventoryService.findRawInventoriesByProductId(productId);

        if (inventories == null || inventories.isEmpty()) {
            throw new ServiceException(
                    EnumError.INVENTORY_ERR_NOT_FOUND_PRODUCT_ID,
                    "inventory.not.found.product.id",
                    Map.of("id", productId)
            );
        }
    }


}
