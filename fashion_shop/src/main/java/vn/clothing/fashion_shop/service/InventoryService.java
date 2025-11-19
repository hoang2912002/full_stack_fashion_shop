package vn.clothing.fashion_shop.service;

import java.util.List;

import vn.clothing.fashion_shop.domain.Inventory;
import vn.clothing.fashion_shop.web.rest.DTO.requests.InventoryRequest.BaseInventoryRequest;

public interface InventoryService {
    List<Inventory> findRawListInventoryBySku(List<Long> skuIds);
    List<Inventory> createListInventory(List<Inventory> inventories);
    List<Inventory> lockInventoryBySkuId(List<Long> skuIds);
    boolean existsByProductSkuId(Long id);

    // Import stock: tăng quantity, tạo transaction IMPORT
    void importStock(List<BaseInventoryRequest> imports, String operator);

    // Update stock: tăng quantity, tạo transaction EXPORT
    void adjustmentStock(List<BaseInventoryRequest> adjustments, String operator);
}
