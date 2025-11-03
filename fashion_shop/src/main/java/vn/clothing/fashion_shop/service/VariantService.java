package vn.clothing.fashion_shop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.clothing.fashion_shop.domain.Variant;
import vn.clothing.fashion_shop.repository.VariantRepository;

@Service
public class VariantService {
    
    private final VariantRepository variantRepository;

    public VariantService(VariantRepository variantRepository) {
        this.variantRepository = variantRepository;
    }

    public List<Variant> createListVariant(List<Variant> variant){
        return this.variantRepository.saveAllAndFlush(variant);
    }
    
    public Variant createVariant(Variant variant){
        return this.variantRepository.saveAndFlush(variant);
    }

    public Variant findVariantFromProduct(Variant variant){
        Optional<Variant> check = this.variantRepository.findByProductIdAndSkuIdAndOptionIdAndOptionValueId(variant.getProduct(),variant.getSku(),variant.getOption(),variant.getOptionValue());
        return check.isPresent() ? check.get() : null;
    }
}
