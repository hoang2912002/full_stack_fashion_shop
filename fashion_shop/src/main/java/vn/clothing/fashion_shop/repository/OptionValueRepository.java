package vn.clothing.fashion_shop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.clothing.fashion_shop.domain.OptionValue;

public interface OptionValueRepository extends JpaRepository<OptionValue, Long>, JpaSpecificationExecutor<OptionValue> {
    Optional<OptionValue> findBySlug(String slug);
    Optional<OptionValue> findBySlugAndIdNot(String slug, Long id);

    List<OptionValue> findAllByIdIn(List<Long> id);     
    List<OptionValue> findAllBySlugIn(List<String> slug);     
}
