package vn.clothing.fashion_shop.service.impls;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.constants.enumEntity.ApprovalMasterEnum;
import vn.clothing.fashion_shop.constants.util.SlugUtil;
import vn.clothing.fashion_shop.domain.ApprovalHistory;
import vn.clothing.fashion_shop.domain.ApprovalMaster;
import vn.clothing.fashion_shop.domain.ShopManagement;
import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.mapper.ShopManagementMapper;
import vn.clothing.fashion_shop.repository.ApprovalMasterRepository;
import vn.clothing.fashion_shop.repository.ShopManagementRepository;
import vn.clothing.fashion_shop.service.ApprovalHistoryService;
import vn.clothing.fashion_shop.service.ApprovalMasterService;
import vn.clothing.fashion_shop.service.ShopManagementService;
import vn.clothing.fashion_shop.service.UserService;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ShopManagementResponse;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopManagementServiceImpl implements ShopManagementService{

    private final ShopManagementRepository shopManagementRepository;
    private final ShopManagementMapper shopManagementMapper;
    private final ApprovalHistoryService approvalHistoryService;
    private final ApprovalMasterService approvalMasterService;
    private final UserService userService;
    
    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public ShopManagementResponse createShopManagement(ShopManagement shopManagement) {
        log.info("[createShopManagement] Start create shop management");
        try {
            User user = this.userService.findRawUserById(shopManagement.getUser().getId());
            String userRole = user.getRole().getSlug().toUpperCase();
            if(!userRole.equals(RoleServiceImpl.roleADMIN) && !userRole.equals(RoleServiceImpl.roleSELLER)){
                throw new ServiceException(
                EnumError.PERMISSION_ACCESS_DENIED, // add this enum if missing
                "permission.access.deny");
            }
            String slug = SlugUtil.toSlug(shopManagement.getName());
            ShopManagement smCheckName =  this.findShopManagementBySlug(slug);
            if(smCheckName != null){
                throw new ServiceException(
                    EnumError.SHOP_MANAGEMENT_DATA_EXISTED_NAME, // add this enum if missing
                    "shop.management.exist.name",
                    Map.of("name", shopManagement.getName())
                );
            }
            shopManagement.setSlug(slug);
            shopManagement.setUser(user);
            ShopManagement smCreate = this.shopManagementRepository.saveAndFlush(shopManagement);
            
            // Mặc định tạo mới gian hàng với trạng thái pending nên skip check data phê duyệt
            this.approvalHistoryService.createApprovalHistory(
                ApprovalHistory.builder()
                    .approvalMaster(new ApprovalMaster())
                    .approvedAt(Instant.now())
                    .requestId(smCreate.getId())
                    .note("")
                    .build(), 
                true, 
                ApprovalHistoryServiceImpl.ENTITY_TYPE_SHOP_MANAGEMENT);
            return shopManagementMapper.toDto(smCreate);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[createShopManagement] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public ShopManagementResponse updateShopManagement(ShopManagement shopManagement) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateShopManagement'");
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class, readOnly = true)
    public ShopManagementResponse getShopManagementById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getShopManagementById'");
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class, readOnly = true)
    public PaginationResponse getAllShopManagement(Pageable pageable, Specification specification) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllShopManagement'");
    }

    @Override
    public void deleteShopManagementById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteShopManagementById'");
    }
    
    @Override
    @Transactional(readOnly = true)
    public ShopManagement findShopManagementBySlug(String slug){
        try {
            Optional<ShopManagement> opt = this.shopManagementRepository.findBySlug(slug);
            return opt.isPresent() ? opt.get() : null;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[findShopManagementBySlug] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
}
