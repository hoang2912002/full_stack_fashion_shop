package vn.clothing.fashion_shop.service;

import java.util.List;

import vn.clothing.fashion_shop.domain.Inventory;
import vn.clothing.fashion_shop.domain.InventoryTransaction;

public interface InventoryTransactionService {
    List<InventoryTransaction> findRawListInventoryTransactionBySku(List<Long> skuIds);
    List<InventoryTransaction> createListInventoryTransaction(List<InventoryTransaction> inventoryTransactions);
}
