package vn.clothing.fashion_shop.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.clothing.fashion_shop.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    Optional<Category> findBySlug(String slug);
    Optional<Category> findBySlugAndIdNot(String slug, Long id);
    List<Category> findAllByIdIn(List<Long> id);
    List<Category> findAllByIdNotIn(Collection<Long> id);

    // @Query("SELECT c FROM Category c LEFT JOIN FETCH c.children WHERE c.id = :id")
    @EntityGraph(attributePaths = "children")
    @Query("SELECT DISTINCT c FROM Category c WHERE c.id IN :ids")
    List<Category> findAllByIdWithChildren(@Param("ids") List<Long> ids);

}
