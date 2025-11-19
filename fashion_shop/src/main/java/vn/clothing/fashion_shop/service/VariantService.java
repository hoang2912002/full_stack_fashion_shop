package vn.clothing.fashion_shop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.clothing.fashion_shop.domain.Variant;
import vn.clothing.fashion_shop.repository.VariantRepository;

public interface VariantService {
    List<Variant> createListVariant(List<Variant> variant);
    Variant createVariant(Variant variant);
    Variant findVariantFromProduct(Variant variant);
    void deleteAllVariantByProductId(Long productId);
}
