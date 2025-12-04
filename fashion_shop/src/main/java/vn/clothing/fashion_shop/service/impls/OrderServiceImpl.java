package vn.clothing.fashion_shop.service.impls;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.domain.Order;
import vn.clothing.fashion_shop.service.OrderService;
import vn.clothing.fashion_shop.web.rest.DTO.responses.OrderResponse;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Override
    public OrderResponse createOrder(Order order) {
        try {
            return null;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[createOrder] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    public OrderResponse updateOrder(Order order) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateOrder'");
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOrderById'");
    }

    @Override
    public OrderResponse getAllOrder(Pageable pageable, Specification specification) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllOrder'");
    }

    @Override
    public void deleteOrderById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteOrderById'");
    }
    
}
