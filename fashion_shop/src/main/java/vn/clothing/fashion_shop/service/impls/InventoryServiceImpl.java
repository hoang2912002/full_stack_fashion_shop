package vn.clothing.fashion_shop.service.impls;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.domain.Inventory;
import vn.clothing.fashion_shop.repository.InventoryRepository;
import vn.clothing.fashion_shop.service.InventoryService;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;

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
}
