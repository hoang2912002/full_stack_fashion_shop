package vn.clothing.fashion_shop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import vn.clothing.fashion_shop.domain.ShopManagement;


public interface ShopManagementRepository extends JpaRepository<ShopManagement, Long>, JpaSpecificationExecutor<ShopManagement>{
    Optional<ShopManagement> findBySlug(String slug);
    Optional<ShopManagement> findBySlugAndIdNot(String slug, Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM ShopManagement p WHERE p.id = :id")
    @QueryHints({
        @QueryHint(name = "javax.persistence.lock.timeout", value = "0"),
        @QueryHint(name = "org.hibernate.readOnly", value = "false") // do write data nên cần xử lý transaction readOnLy false tránh Hibernate tối ưu sai
    })
    Optional<ShopManagement> findByIdForUpdate(@Param("id") Long id);
}
