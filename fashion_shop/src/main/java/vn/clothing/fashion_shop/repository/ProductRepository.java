package vn.clothing.fashion_shop.repository;

import java.util.List;
import java.util.Optional;

import javax.swing.ListModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import vn.clothing.fashion_shop.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>{
    Optional<Product> findBySlug(String slug);
    Optional<Product> findBySlugAndIdNot(String slug, Long id);
    List<Product> findAllByIdIn(List<Long> id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    @QueryHints({
        @QueryHint(name = "javax.persistence.lock.timeout", value = "0") // 0 = fail immediately
    })
    Product lockProductById(@Param("id") Long id);
}
