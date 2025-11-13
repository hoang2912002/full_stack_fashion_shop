package vn.clothing.fashion_shop.repository;

import java.util.List;
import java.util.Optional;

import javax.swing.ListModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.clothing.fashion_shop.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>{
    Optional<Product> findBySlug(String slug);
    Optional<Product> findBySlugAndIdNot(String slug, Long id);
    List<Product> findAllByIdIn(List<Long> id);
}
