package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.domain.ShopManagement;
import vn.clothing.fashion_shop.mapper.ShopManagementMapper;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ShopManagementRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ShopManagementResponse;
@RestController
@RequestMapping("/api/v1/admin/shopManagements")
@RequiredArgsConstructor
public class ShopManagementController {
    private final ShopManagementMapper shopManagementMapper;
    @PostMapping("")
    @ApiMessageResponse("shop.management.success.create")
    public ResponseEntity<ShopManagementResponse> createShopManagement(
        @RequestBody @Valid ShopManagementRequest shopManagementRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping("")
    @ApiMessageResponse("shop.management.success.update")
    public ResponseEntity<ShopManagementResponse> updateShopManagement(
        @RequestBody @Valid ShopManagementRequest shopManagementRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/{id}")
    @ApiMessageResponse("shop.management.success.get.single")
    public ResponseEntity<ShopManagementResponse> getShopManagementById(
        @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("")
    @ApiMessageResponse("shop.management.success.get.all")
    public ResponseEntity<PaginationResponse> getAllShopManagement(
        Pageable pageable,
        @Filter Specification<ShopManagement> spec
    ){
        return ResponseEntity.ok(null);
    }
    
    @DeleteMapping("/{id}")
    @ApiMessageResponse("shop.management.success.delete")
    public ResponseEntity<Void> deleteShopManagementById(
        @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(null);
    }
}
