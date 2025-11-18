package vn.clothing.fashion_shop.service.impls;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.domain.InventoryTransaction;
import vn.clothing.fashion_shop.repository.InventoryTransactionRepository;
import vn.clothing.fashion_shop.service.InventoryTransactionService;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryTransactionImpl implements InventoryTransactionService {
    private final InventoryTransactionRepository inventoryTransactionRepository;
    @Override
    @Transactional(readOnly = true)
    public List<InventoryTransaction> findRawListInventoryTransactionBySku(List<Long> skuIds){
        try {
            return null;
        } catch (Exception e) {
            log.error("[findRawListInventoryTransactionBySku] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public List<InventoryTransaction> createListInventoryTransaction(List<InventoryTransaction> inventoryTransactions){
        try {
            return this.inventoryTransactionRepository.saveAllAndFlush(inventoryTransactions);
        } catch (Exception e) {
            log.error("[findRawListInventoryTransactionBySku] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
}
