package vn.clothing.fashion_shop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.domain.Inventory;
import vn.clothing.fashion_shop.domain.OrderDetail;
import vn.clothing.fashion_shop.domain.ProductSku;
import vn.clothing.fashion_shop.repository.ProductSkuRepository;

@Service
public interface ProductSkuService {
    List<ProductSku> findListProductSku(List<String> productSkus);
    List<ProductSku> findListProductSkuById(List<Long> ids);
    List<ProductSku> findListProductSkuByProductId(Long id);
    ProductSku findRawProductSkuById(Long id);
    List<ProductSku> createListProductSku(List<ProductSku> productSkus);
    void deleteProductSkuById(Long id);
    void deleteProductSkuByListId(List<Long> ids);
}
