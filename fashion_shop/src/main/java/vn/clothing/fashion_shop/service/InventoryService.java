package vn.clothing.fashion_shop.service;

import java.util.List;

import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import vn.clothing.fashion_shop.domain.Inventory;
import vn.clothing.fashion_shop.web.rest.DTO.requests.InventoryRequest.BaseInventoryRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.InventoryResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;

public interface InventoryService {
    List<Inventory> findRawListInventoryBySku(List<Long> skuIds);
    List<Inventory> createListInventory(List<Inventory> inventories);
    List<Inventory> lockInventoryBySkuId(List<Long> skuIds);
    boolean existsByProductSkuId(Long id);
    void importStock(List<BaseInventoryRequest> imports, String operator);
    void adjustmentStock(List<BaseInventoryRequest> adjustments, String operator);

    InventoryResponse createInventory(Inventory inventory);
    InventoryResponse updateInventory(Inventory inventory);
    InventoryResponse getInventoryById(Long id);
    PaginationResponse getAllInventories(Pageable pageable, Specification specification);
    void deleteInventoryById(Long id);
}
