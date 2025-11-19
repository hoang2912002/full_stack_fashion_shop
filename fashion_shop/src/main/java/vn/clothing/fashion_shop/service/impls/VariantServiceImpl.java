package vn.clothing.fashion_shop.service.impls;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.domain.Variant;
import vn.clothing.fashion_shop.repository.VariantRepository;
import vn.clothing.fashion_shop.service.VariantService;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;

@Service
@RequiredArgsConstructor
@Slf4j
public class VariantServiceImpl implements VariantService {
    private final VariantRepository variantRepository;

    @Override
    @Transactional(rollbackFor =  ServiceException.class)
    public List<Variant> createListVariant(List<Variant> variant){
        log.info("[createListVariant] start create Variant ....");
        try {
            return this.variantRepository.saveAllAndFlush(variant);
        } catch (Exception e) {
            log.error("[createListVariant] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
    
    @Override
    @Transactional(rollbackFor =  ServiceException.class)
    public Variant createVariant(Variant variant){
        try {
            return this.variantRepository.saveAndFlush(variant);
        } catch (Exception e) {
            log.error("[createVariant] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Variant findVariantFromProduct(Variant variant){
        try {
            Optional<Variant> check = this.variantRepository.findByProductIdAndSkuIdAndOptionIdAndOptionValueId(variant.getProduct(),variant.getSku(),variant.getOption(),variant.getOptionValue());
            return check.isPresent() ? check.get() : null;
        } catch (Exception e) {
            log.error("[createVariant] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackFor =  ServiceException.class)
    public void deleteAllVariantByProductId(Long productId){
        try {
            this.variantRepository.deleteAllByProductId(productId);
        } catch (Exception e) {
            log.error("[deleteAllVariantByProductId] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
}
