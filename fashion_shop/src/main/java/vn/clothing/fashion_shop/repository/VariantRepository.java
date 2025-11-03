package vn.clothing.fashion_shop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.clothing.fashion_shop.domain.Option;
import vn.clothing.fashion_shop.domain.OptionValue;
import vn.clothing.fashion_shop.domain.Product;
import vn.clothing.fashion_shop.domain.ProductSku;
import vn.clothing.fashion_shop.domain.Variant;

public interface VariantRepository extends JpaRepository<Variant, Long>, JpaSpecificationExecutor<Variant>{
    Optional<Variant> findByProductIdAndSkuIdAndOptionIdAndOptionValueId(
        Product product,
        ProductSku sku,
        Option option,
        OptionValue optionValue
    );
}
