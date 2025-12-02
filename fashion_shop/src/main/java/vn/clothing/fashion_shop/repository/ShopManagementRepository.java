package vn.clothing.fashion_shop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.clothing.fashion_shop.domain.ShopManagement;


public interface ShopManagementRepository extends JpaRepository<ShopManagement, Long>, JpaSpecificationExecutor<ShopManagement>{
    Optional<ShopManagement> findBySlug(String slug);
}
