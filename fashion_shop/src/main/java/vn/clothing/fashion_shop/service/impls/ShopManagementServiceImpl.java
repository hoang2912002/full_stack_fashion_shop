package vn.clothing.fashion_shop.service.impls;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.constants.enumEntity.ApprovalMasterEnum;
import vn.clothing.fashion_shop.constants.util.ConvertPagination;
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
import vn.clothing.fashion_shop.web.rest.DTO.responses.RoleResponse;
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

    public static final Set<String> IMPORTANT_FIELDS = Set.of(
        "accountName",
        "accountNumber",
        "bankBranch",
        "bankName",
        "businessDateIssue",
        "businessLicence",
        "businessName",
        "businessNo",
        "businessType",
        "businessPlace",
        "taxCode",
        "identificationImageFirst",
        "identificationImageSecond",
        "name"
    );

    public static final Set<String> NORMAL_FIELDS = Set.of(
        "description",
        "address",
        "thumbnail",
        "logo"
    );

    private static final Set<String> IGNORE_FIELDS = Set.of(
        "products",
        "user",
        "slug"
    );

    
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
            ShopManagement smCheckName =  this.findRawShopManagementBySlug(slug, null);
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
        log.info("[updateShopManagement] Start create shop management");
        try {
            // 1. Lấy khóa WRITE
            ShopManagement uShopManagement = this.findRawShopManagementByIdForUpdate(shopManagement.getId());
            if(uShopManagement == null){
                throw new ServiceException(
                EnumError.SHOP_MANAGEMENT_ERR_NOT_FOUND_ID, // add this enum if missing
                "shop.management.not.found.id");
            }
            // 2. Kiểm tra quyền user
            User user = this.userService.findRawUserById(shopManagement.getUser().getId());
            String userRole = user.getRole().getSlug().toUpperCase();
            if(!userRole.equals(RoleServiceImpl.roleADMIN) && !userRole.equals(RoleServiceImpl.roleSELLER)){
                throw new ServiceException(
                EnumError.PERMISSION_ACCESS_DENIED, // add this enum if missing
                "permission.access.deny");
            }
            // 3. Check slug
            String slug = SlugUtil.toSlug(shopManagement.getName());
            if(!slug.equals(uShopManagement.getSlug())){
                ShopManagement smCheckName =  this.findRawShopManagementBySlug(slug, null);
                if(smCheckName != null){
                    throw new ServiceException(
                        EnumError.SHOP_MANAGEMENT_DATA_EXISTED_NAME, // add this enum if missing
                        "shop.management.exist.name",
                        Map.of("name", shopManagement.getName())
                    );
                }
            }
            // 4. Kiểm tra có field quan trọng thay đổi không
            Map<String, Object[]> changedFields = detectChangedFields(uShopManagement, shopManagement);
            
            boolean isImportantChanged = changedFields.keySet()
                .stream()
                .anyMatch(IMPORTANT_FIELDS::contains);

            ShopManagement target = uShopManagement; // entity được update thực tế
            // 5. Kiểm tra field quan trọng thay đổi
            if(isImportantChanged){
                // 5A. Kiểm tra quy trình phê duyệt nếu PENDING thì cho cập nhật hết

                boolean check =  this.approvalHistoryService.checkApprovalHistoryForUpShop(uShopManagement);
                if(check){
                    //Java reference semantics
                    copyAllFields(uShopManagement, shopManagement);
                    target.setSlug(slug);
                    target.setUser(user);
                }
            } else{
                // 5B. Nếu ko có field quan trọng update bình thường
                applyNormalFields(uShopManagement, shopManagement);
                target.setSlug(slug);
                target.setUser(user);
            }
            ShopManagement saved = shopManagementRepository.saveAndFlush(target);
            return shopManagementMapper.toDto(saved);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[updateShopManagement] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class, readOnly = true)
    public ShopManagementResponse getShopManagementById(Long id) {
        try {
            ShopManagement shopManagement = this.findRawShopManagementById(id);
            if(shopManagement == null){
                throw new ServiceException(
                    EnumError.SHOP_MANAGEMENT_ERR_NOT_FOUND_ID, // add this enum if missing
                    "shop.management.not.found.id",
                    Map.of("id", id)
                );
            }
            return shopManagementMapper.toDto(shopManagement);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[getShopManagementById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class, readOnly = true)
    public PaginationResponse getAllShopManagement(Pageable pageable, Specification specification) {
        try {
            Page<ShopManagement> shopManagements = this.shopManagementRepository.findAll(specification, pageable);
            List<ShopManagementResponse> sManagementResponses = shopManagements.getContent().stream().map(s -> {
                return shopManagementMapper.toDto(s);
            }).toList();
            return ConvertPagination.handleConvert(pageable, shopManagements, sManagementResponses);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[findShopManagementBySlug] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    public void deleteShopManagementById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteShopManagementById'");
    }
    
    @Override
    @Transactional(readOnly = true)
    public ShopManagement findRawShopManagementBySlug(String slug, Long checkId){
        try {
            Optional<ShopManagement> opt = checkId == null 
                ? this.shopManagementRepository.findBySlug(slug)
                : this.shopManagementRepository.findBySlugAndIdNot(slug, checkId);
            return opt.isPresent() ? opt.get() : null;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[findShopManagementBySlug] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ShopManagement findRawShopManagementById(Long id){
        try {
            Optional<ShopManagement> opt =  this.shopManagementRepository.findById(id);
            return opt.isPresent() ? opt.get() : null;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[findRawShopManagementById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
    
    @Override
    @Transactional(readOnly = false)
    public ShopManagement findRawShopManagementByIdForUpdate(Long id){
        try {
            Optional<ShopManagement> opt =  this.shopManagementRepository.findByIdForUpdate(id);
            return opt.isPresent() ? opt.get() : null;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[findRawShopManagementByIdForUpdate] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    public Map<String, Object[]> detectChangedFields(ShopManagement oldData, ShopManagement newData) {
        Map<String, Object[]> changes = new HashMap<>();
        try {
            //Field là đối tượng của Reflection API, đại diện cho một thuộc tính (field) trong class, bất kể kiểu dữ liệu là gì: String, Integer, Instant, UUID...
            //getDeclaredFields() sẽ trả về mảng tất cả các field được khai báo trực tiếp trong class đó, bao gồm cả private, protected, public.
            for (Field field : ShopManagement.class.getDeclaredFields()) {
                if(IGNORE_FIELDS.contains(field.getName())){
                    continue;
                }
                field.setAccessible(true);
                Object oldValue = field.get(oldData);
                Object newValue = field.get(newData);

                if (!Objects.equals(oldValue, newValue)) {
                    changes.put(field.getName(), new Object[]{oldValue, newValue});
                }
            }
        } catch (Exception e) {
            log.error("[detectChangedFields] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }

        return changes;
    }


    private void copyAllFields(ShopManagement target, ShopManagement source) {
        target.setAccountName(source.getAccountName());
        target.setAccountNumber(source.getAccountNumber());
        target.setAddress(source.getAddress());
        target.setBankName(source.getBankName());
        target.setBankBranch(source.getBankBranch());
        target.setBusinessDateIssue(source.getBusinessDateIssue());
        target.setBusinessLicence(source.getBusinessLicence());
        target.setBusinessName(source.getBusinessName());
        target.setBusinessNo(source.getBusinessNo());
        target.setBusinessType(source.getBusinessType());
        target.setBusinessPlace(source.getBusinessPlace());
        target.setDescription(source.getDescription());
        target.setIdentificationImageFirst(source.getIdentificationImageFirst());
        target.setIdentificationImageSecond(source.getIdentificationImageSecond());
        target.setLogo(source.getLogo());
        target.setName(source.getName());
        target.setTaxCode(source.getTaxCode());
        target.setThumbnail(source.getThumbnail());
    }

    private void applyNormalFields(ShopManagement target, ShopManagement source) {
        target.setDescription(source.getDescription());
        target.setThumbnail(source.getThumbnail());
        target.setLogo(source.getLogo());
        target.setAddress(source.getAddress());
    }

}
