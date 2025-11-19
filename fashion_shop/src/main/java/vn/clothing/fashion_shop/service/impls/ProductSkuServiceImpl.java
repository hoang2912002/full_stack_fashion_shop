package vn.clothing.fashion_shop.service.impls;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.domain.ProductSku;
import vn.clothing.fashion_shop.repository.InventoryRepository;
import vn.clothing.fashion_shop.repository.ProductSkuRepository;
import vn.clothing.fashion_shop.service.InventoryService;
import vn.clothing.fashion_shop.service.OrderDetailService;
import vn.clothing.fashion_shop.service.ProductSkuService;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSkuServiceImpl implements ProductSkuService {
    private final ProductSkuRepository productSkuRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderDetailService orderDetailService;

    public List<ProductSku> findListProductSku(List<String> productSkus){
        return this.productSkuRepository.findAllBySkuIn(productSkus);
    }
    
    public List<ProductSku> findListProductSkuById(List<Long> ids){
        return this.productSkuRepository.findAllByIdIn(ids);
    }
    
    public List<ProductSku> findListProductSkuByProductId(Long id){
        return this.productSkuRepository.findAllByProductId(id);
    }

    public ProductSku findRawProductSkuById(Long id){
        Optional<ProductSku> productSku = this.productSkuRepository.findById(id);
        return productSku.isPresent() ? productSku.get() : null;
    }

    public List<ProductSku> createListProductSku(List<ProductSku> productSkus){
        return this.productSkuRepository.saveAllAndFlush(productSkus);
    }

    public void deleteProductSkuById(Long id){
        this.productSkuRepository.deleteById(id);
    }
    
    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void deleteProductSkuByListId(List<Long> ids){
        try {
            for(Long id: ids){
                boolean inv = this.inventoryRepository.existsByProductSkuId(id);
                boolean oDetail = this.orderDetailService.existsByProductSkuId(id);
                if(!inv && !oDetail){
                    this.productSkuRepository.deleteById(id);
                }            
            }
            
        } catch (Exception e) {
            log.error("[deleteProductSkuByListId] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

}
