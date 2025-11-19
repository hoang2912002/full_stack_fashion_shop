package vn.clothing.fashion_shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import vn.clothing.fashion_shop.domain.ProductSku;

public interface ProductSkuRepository extends JpaRepository<ProductSku, Long>, JpaSpecificationExecutor<ProductSku> {
    List<ProductSku> findAllBySkuIn(List<String> sku);
    List<ProductSku> findAllByIdIn(List<Long> id);
    List<ProductSku> findAllByProductId(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ProductSku s WHERE s.product.id = :productId")
    List<ProductSku> lockSkuByProduct(@Param("productId") Long productId);
}
