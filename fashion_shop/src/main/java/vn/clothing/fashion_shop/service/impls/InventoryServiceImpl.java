package vn.clothing.fashion_shop.service.impls;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.constants.enumEntity.InventoryTransactionTypeEnum;
import vn.clothing.fashion_shop.domain.Inventory;
import vn.clothing.fashion_shop.domain.InventoryTransaction;
import vn.clothing.fashion_shop.domain.ProductSku;
import vn.clothing.fashion_shop.mapper.InventoryMapper;
import vn.clothing.fashion_shop.repository.InventoryRepository;
import vn.clothing.fashion_shop.service.InventoryService;
import vn.clothing.fashion_shop.service.InventoryTransactionService;
import vn.clothing.fashion_shop.service.ProductSkuService;
import vn.clothing.fashion_shop.web.rest.DTO.requests.InventoryRequest.BaseInventoryRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.InventoryResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final ProductSkuService productSkuService;
    private final InventoryTransactionService inventoryTransactionService;
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> findRawListInventoryBySku(List<Long> skuIds) {
        try {
            return this.inventoryRepository.findAllByProductSkuIdIn(skuIds);
        } catch (Exception e) {
            log.error("[findRawListInventoryBySku] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByProductSkuId(Long id){
        try {
            return this.inventoryRepository.existsByProductSkuId(id);
        } catch (Exception e) {
            log.error("[existsByProductSkuId] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
    
    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public List<Inventory> createListInventory(List<Inventory> inventories){
        log.info("[createListInventory] start create list inventories ....");
        try {
            return this.inventoryRepository.saveAllAndFlush(inventories);
        } catch (Exception e) {
            log.error("[createListInventory] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public List<Inventory> lockInventoryBySkuId(List<Long> skuIds) {
        try {
            return this.inventoryRepository.lockInventoryBySkuId(skuIds);
        } catch (Exception e) {
            log.error("[lockInventoryBySkuId] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void importStock(List<BaseInventoryRequest> inventories, String operator){
        log.info("[importStock] start import stock from Inventory ....");
        try {
            processInventoryChange(inventories, InventoryTransactionTypeEnum.IMPORT, true);
        } catch (Exception e) {
            log.error("[importStock] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void adjustmentStock(List<BaseInventoryRequest> inventories, String operator){
        log.info("[adjustmentStock] start adjustment stock from Inventory ....");
        try {
            processInventoryChange(inventories, InventoryTransactionTypeEnum.ADJUSTMENT, false);
        } catch (Exception e) {
            log.error("[adjustmentStock] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    private void processInventoryChange(
        List<BaseInventoryRequest> inventories,
        InventoryTransactionTypeEnum type,
        boolean allowCreate
    ) {
        if (inventories == null || inventories.isEmpty()) return;

        List<Long> skuIds = inventories.stream().map(BaseInventoryRequest::getSkuId).toList();

        this.lockInventoryBySkuId(skuIds);

        Map<Long, Inventory> inventoryMap = findRawListInventoryBySku(skuIds)
            .stream().collect(Collectors.toMap(inv -> inv.getProductSku().getId(), Function.identity()));

        Map<Long, ProductSku> productSkuMap = this.productSkuService.findListProductSkuById(skuIds)
            .stream().collect(Collectors.toMap(ProductSku::getId, Function.identity()));

        List<Inventory> toSaveInventories = new ArrayList<>();
        List<InventoryTransaction> toSaveTransactions = new ArrayList<>();

        for (BaseInventoryRequest req : inventories) {

            ProductSku sku = productSkuMap.get(req.getSkuId());
            if (sku == null) {
                log.warn("SKU not found: {}", req.getSkuId());
                continue;
            }

            Inventory invDB = inventoryMap.get(req.getSkuId());
            if (invDB == null && !allowCreate) {
                log.warn("Inventory not found for sku={}, adjustment skipped", req.getSkuId());
                continue;
            }

            // Before & After
            int before = invDB != null && invDB.getQuantityAvailable() != null
                    ? invDB.getQuantityAvailable()
                    : 0;

            int after = before + req.getQuantity();

            // Build inventory entity
            Inventory invEntity = Inventory.builder()
                .id(invDB != null ? invDB.getId() : null)
                .activated(true)
                .product(sku.getProduct())
                .productSku(sku)
                .quantityAvailable(after)
                .quantityReserved(invDB != null ? invDB.getQuantityReserved() : 0)
                .quantitySold(invDB != null ? invDB.getQuantitySold() : 0)
                .build();

            toSaveInventories.add(invEntity);

            // Build transaction
            InventoryTransaction tx = InventoryTransaction.builder()
                .activated(true)
                .productSku(sku)
                .beforeQuantity(before)
                .afterQuantity(after)
                .quantityChange(req.getQuantity())
                .type(type)
                .referenceType(req.getReferenceType())
                .referenceId(req.getReferenceId())
                .note(req.getNote())
                .build();

            toSaveTransactions.add(tx);
        }
        
        // Save
        this.inventoryRepository.saveAllAndFlush(toSaveInventories);
        this.inventoryTransactionService.createListInventoryTransaction(toSaveTransactions);
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public InventoryResponse createInventory(Inventory inventory) {
        log.info("[createInventory] start create inventory ....");
        try {
            return null;
        } catch (Exception e) {
            log.error("[createInventory] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    public InventoryResponse updateInventory(Inventory inventory) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateInventory'");
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getInventoryById(Long id) {
        try {
            Inventory inventory = this.findRawInventoryById(id);
            if (inventory == null) {
                throw new ServiceException(EnumError.INVENTORY_ERR_NOT_FOUND_ID, "inventory.not.found.id");
            }
            return inventoryMapper.toDto(inventory);
        } catch (Exception e) {
            log.error("[getInventoryById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    public PaginationResponse getAllInventories(Pageable pageable, Specification specification) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllInventories'");
    }

    @Override
    public void deleteInventoryById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteInventoryById'");
    }

    @Override
    @Transactional(readOnly = true)
    public Inventory findRawInventoryById(Long id) {
        try {
            Optional<Inventory> inventoryOpt = this.inventoryRepository.findById(id);
            return inventoryOpt.isPresent() ? inventoryOpt.get() : null;
        } catch (Exception e) {
            log.error("[findRawInventoryById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Integer countTotalInventories(Long productId) {
        try {
            return this.inventoryRepository.countByProductId(productId);
        } catch (Exception e) {
            log.error("[countTotalInventories] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> findRawInventoriesByProductId(Long productId) {
        try {
            return this.inventoryRepository.findAllByProductId(productId);
        } catch (Exception e) {
            log.error("[findRawInventoriesByProductId] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
}
