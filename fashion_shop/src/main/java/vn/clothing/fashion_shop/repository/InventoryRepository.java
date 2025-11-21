package vn.clothing.fashion_shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import vn.clothing.fashion_shop.domain.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long>, JpaSpecificationExecutor<Inventory> {
    List<Inventory> findAllByProductSkuIdIn(List<Long> skuIds);
    boolean existsByProductSkuId(Long id);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Inventory i WHERE i.productSku.id IN :skuIds")
    List<Inventory> lockInventoryBySkuId(@Param("skuIds") List<Long> skuIds);
    Integer countByProductId(Long productId);
}
