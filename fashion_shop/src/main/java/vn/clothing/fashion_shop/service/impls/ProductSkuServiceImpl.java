package vn.clothing.fashion_shop.service.impls;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.constants.util.MessageUtil;
import vn.clothing.fashion_shop.constants.util.SlugUtil;
import vn.clothing.fashion_shop.domain.Inventory;
import vn.clothing.fashion_shop.domain.Product;
import vn.clothing.fashion_shop.domain.ProductSku;
import vn.clothing.fashion_shop.mapper.ProductSkuMapper;
import vn.clothing.fashion_shop.repository.InventoryRepository;
import vn.clothing.fashion_shop.repository.ProductSkuRepository;
import vn.clothing.fashion_shop.service.InventoryService;
import vn.clothing.fashion_shop.service.OrderDetailService;
import vn.clothing.fashion_shop.service.ProductSkuService;
import vn.clothing.fashion_shop.web.rest.DTO.requests.InventoryRequest.BaseInventoryRequest;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSkuServiceImpl implements ProductSkuService {
    private final ProductSkuRepository productSkuRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderDetailService orderDetailService;
    private final InventoryService inventoryService;
    private final ProductSkuMapper productSkuMapper;
    private final MessageUtil messageUtil;
    @Override
    @Transactional(readOnly = true)
    public List<ProductSku> findListProductSku(List<String> productSkus){
        try {
            return this.productSkuRepository.findAllBySkuIn(productSkus);
        } catch (Exception e) {
            log.error("[findListProductSku] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductSku> findListProductSkuById(List<Long> ids){
        try {
            return this.productSkuRepository.findAllByIdIn(ids);
        } catch (Exception e) {
            log.error("[findListProductSkuById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductSku> findListProductSkuByProductId(Long id){
        try {
            return this.productSkuRepository.findAllByProductId(id);            
        } catch (Exception e) {
            log.error("[findListProductSkuByProductId] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProductSku findRawProductSkuById(Long id){
        try {
            Optional<ProductSku> productSku = this.productSkuRepository.findById(id);
            return productSku.isPresent() ? productSku.get() : null;
        } catch (Exception e) {
            log.error("[findListProductSkuByProductId] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductSku> createListProductSku(List<ProductSku> productSkus){
        try {
            return this.productSkuRepository.saveAllAndFlush(productSkus);
        } catch (Exception e) {
            log.error("[createListProductSku] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void deleteProductSkuById(Long id){
        try {
            this.productSkuRepository.deleteById(id);
        } catch (Exception e) {
            log.error("[createListProductSku] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
    
    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void deleteProductSkuByListId(List<Long> ids){
        try {
            for(Long id: ids){
                boolean inv = this.inventoryRepository.existsByProductSkuId(id);
                boolean oDetail = this.orderDetailService.existsByProductSkuId(id);
                if(!inv && !oDetail){
                    this.productSkuRepository.deleteById(id);
                }            
            }
        } catch (Exception e) {
            log.error("[deleteProductSkuByListId] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void validateAndMapSkusToInventoryRequests(Product product){
        try {
            Locale locale = LocaleContextHolder.getLocale();
            List<BaseInventoryRequest> adjustments = new ArrayList<>();
            List<BaseInventoryRequest> imports = new ArrayList<>();
            List<ProductSku> skus = product.getProductSkus();
            List<Long> skuIds = skus.stream().map(ProductSku::getId).distinct().toList();

            Map<Long, Inventory> inventories = this.inventoryService.findRawListInventoryBySku(
                skuIds
            ).stream().collect(Collectors.toMap(i -> i.getProductSku().getId(), Function.identity(), (a,b) -> a));

            for (ProductSku sku : skus) {
                // int oldStock = skuById.getOrDefault(sku.getId(), sku).getStock() == null ? 0 : skuById.getOrDefault(sku.getId(), sku).getStock();
                int delta = sku.getTempStock();
                if (!inventories.containsKey(sku.getId())) {
                    String note = messageUtil.getMessage("inventory.transaction.import.product", sku.getSku(), locale);
                    imports.add(BaseInventoryRequest.builder()
                        .sku(productSkuMapper.toRequest(sku))
                        .quantity(delta)
                        .referenceType("IMPORT_PRODUCT")
                        .referenceId(sku.getProduct().getId())
                        .note(note)
                        .build());
                } else{
                    String note = messageUtil.getMessage("inventory.transaction.adjustment.product", sku.getSku(), locale);
                    adjustments.add(BaseInventoryRequest.builder()
                        .sku(productSkuMapper.toRequest(sku))
                        .quantity(delta)
                        .referenceType("ADJUSTMENT_PRODUCT") // UPDATE
                        .referenceId(sku.getProduct().getId())
                        .note(note)
                        .build());
                }
            }
            if (!imports.isEmpty()) {
                inventoryService.importStock(imports, "SYSTEM", inventories, product);
            }
            if (!adjustments.isEmpty()) {
                inventoryService.adjustmentStock(adjustments, "SYSTEM", inventories, product);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[validateAndMapSkusToInventoryRequests] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
}
