package vn.clothing.fashion_shop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.clothing.fashion_shop.domain.Option;

public interface OptionRepository extends JpaRepository<Option, Long>, JpaSpecificationExecutor<Option>{
    Optional<Option> findBySlug(String slug);
    Optional<Option> findBySlugAndIdNot(String slug, Long id);
}
