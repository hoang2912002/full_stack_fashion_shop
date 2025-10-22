package vn.clothing.fashion_shop.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.clothing.fashion_shop.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    Optional<Category> findBySlug(String slug);
    Optional<Category> findBySlugAndIdNot(String slug, Long id);

    // Page<Category> findAllParentIsEmpty(Specification specification,Pageable pageable);
}
