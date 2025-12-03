package vn.clothing.fashion_shop.service;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import vn.clothing.fashion_shop.domain.ShopManagement;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ShopManagementResponse;

public interface ShopManagementService {
    ShopManagementResponse createShopManagement(ShopManagement shopManagement);
    ShopManagementResponse updateShopManagement(ShopManagement shopManagement);
    ShopManagementResponse getShopManagementById(Long id);
    PaginationResponse getAllShopManagement(Pageable pageable, Specification specification);
    void deleteShopManagementById(Long id);
    ShopManagement findRawShopManagementBySlug(String slug, Long id);
    ShopManagement findRawShopManagementById(Long id);
    ShopManagement findRawShopManagementByIdForUpdate(Long id);
    Map<String, Object[]> detectChangedFields(ShopManagement oldData, ShopManagement newData);
}
