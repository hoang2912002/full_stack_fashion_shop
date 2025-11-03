package vn.clothing.fashion_shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.clothing.fashion_shop.domain.ProductSku;

public interface ProductSkuRepository extends JpaRepository<ProductSku, Long>, JpaSpecificationExecutor<ProductSku> {
    List<ProductSku> findAllBySkuIn(List<String> sku);
}
