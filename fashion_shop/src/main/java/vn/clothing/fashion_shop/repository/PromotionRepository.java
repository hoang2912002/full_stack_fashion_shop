package vn.clothing.fashion_shop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.clothing.fashion_shop.domain.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Long>, JpaSpecificationExecutor<Promotion>{
    Optional<Promotion> findByCode(String code);
    Optional<Promotion> findByCodeAndIdNot(String code, Long id);
}
