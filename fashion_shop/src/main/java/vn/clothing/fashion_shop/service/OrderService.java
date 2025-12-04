package vn.clothing.fashion_shop.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import vn.clothing.fashion_shop.domain.Order;
import vn.clothing.fashion_shop.web.rest.DTO.responses.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(Order order);
    OrderResponse updateOrder(Order order);
    OrderResponse getOrderById(Long id);
    OrderResponse getAllOrder(Pageable pageable, Specification specification);
    void deleteOrderById(Long id);
}
