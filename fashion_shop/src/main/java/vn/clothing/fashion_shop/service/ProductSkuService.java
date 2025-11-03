package vn.clothing.fashion_shop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.clothing.fashion_shop.domain.ProductSku;
import vn.clothing.fashion_shop.repository.ProductSkuRepository;

@Service
public class ProductSkuService {
    
    private final ProductSkuRepository productSkuRepository;

    public ProductSkuService(ProductSkuRepository productSkuRepository) {
        this.productSkuRepository = productSkuRepository;
    }

    public List<ProductSku> findListProductSku(List<String> productSkus){
        return this.productSkuRepository.findAllBySkuIn(productSkus);
    }

    public List<ProductSku> createListProductSku(List<ProductSku> productSkus){
        return this.productSkuRepository.saveAllAndFlush(productSkus);
    }
}
