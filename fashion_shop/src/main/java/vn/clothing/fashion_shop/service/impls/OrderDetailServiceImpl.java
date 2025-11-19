package vn.clothing.fashion_shop.service.impls;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.repository.OrderDetailRepository;
import vn.clothing.fashion_shop.service.OrderDetailService;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    @Override
    public boolean existsByProductSkuId(Long skuId) {
        try {
            return this.orderDetailRepository.existsByProductSkuId(skuId);
        } catch (Exception e) {
            log.error("[existsByProductSkuId] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
    
}
