package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.domain.Order;
import vn.clothing.fashion_shop.web.rest.DTO.requests.OrderRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.OrderResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/admin/orders")
@RequiredArgsConstructor
public class OrderController {
    
    @PostMapping("")
    @ApiMessageResponse("order.success.create")
    public ResponseEntity<OrderResponse> createOrder(
        @RequestBody OrderRequest orderRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    
    @PutMapping("/{id}")
    @ApiMessageResponse("order.success.update")
    public ResponseEntity<OrderResponse> updateOrder(
        @RequestBody OrderRequest orderRequest
    ) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{id}")
    @ApiMessageResponse("order.success.get.single")
    public ResponseEntity<OrderResponse> getOrderById(
        @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(null);
    }
    
    @GetMapping("")
    @ApiMessageResponse("order.success.get.all")
    public PaginationResponse getAllOrder(Pageable pageable,
        @Filter Specification<Order> spec
    ){
        return null;
    }
    

    @DeleteMapping("/{id}")
    @ApiMessageResponse("order.success.delete")
    public void deleteOrderById(
        @PathVariable("id") Long id
    ) {

    }
}
