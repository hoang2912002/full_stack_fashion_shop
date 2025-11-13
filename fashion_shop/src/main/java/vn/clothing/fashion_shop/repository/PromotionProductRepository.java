package vn.clothing.fashion_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.clothing.fashion_shop.domain.PromotionProduct;


public interface PromotionProductRepository extends JpaRepository<PromotionProduct, Long>, JpaSpecificationExecutor<PromotionProduct> {
    
}
