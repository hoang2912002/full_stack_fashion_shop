package vn.clothing.fashion_shop.service;

import java.util.List;

import vn.clothing.fashion_shop.domain.Inventory;

public interface InventoryService {
    List<Inventory> findRawListInventoryBySku(List<Long> skuIds);
    List<Inventory> createListInventory(List<Inventory> inventories);
    List<Inventory> lockInventoryBySkuId(List<Long> skuIds);
}
