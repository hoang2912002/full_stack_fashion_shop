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
import vn.clothing.fashion_shop.mapper.CouponMapper;
import vn.clothing.fashion_shop.service.CouponService;
import vn.clothing.fashion_shop.web.rest.DTO.requests.CouponRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.CouponResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/coupons")
public class CouponController {
    private final CouponService couponService;
    private final CouponMapper couponMapper;
    @PostMapping("")
    @ApiMessageResponse("coupon.success.create")
    public ResponseEntity<CouponResponse> createCoupon(
        @RequestBody @Valid CouponRequest coupon
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.couponService.createCoupon(this.couponMapper.toValidator(coupon)));
    }

    @PutMapping("")
    @ApiMessageResponse("coupon.success.update")
    public ResponseEntity<CouponResponse> updateCoupon(
        @RequestBody @Valid CouponRequest coupon
    ) {        
        return ResponseEntity.status(HttpStatus.OK).body(this.couponService.updateCoupon(this.couponMapper.toValidator(coupon)));
    }
    
    @GetMapping("/{id}")
    @ApiMessageResponse("coupon.success.get.single")
    public ResponseEntity<CouponResponse> getCouponById(
        @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(this.couponService.getCouponById(id));
    }

    @GetMapping("")
    @ApiMessageResponse("coupon.success.get.all")
    public ResponseEntity<PaginationResponse> getAllCoupon(
        Pageable pageable,
        @Filter Specification spec
    ) {
        return ResponseEntity.ok(this.couponService.getAllCoupon(pageable,spec));
    }
    
    @DeleteMapping("/{id}")
    @ApiMessageResponse("coupon.success.delete")
    public ResponseEntity<Void> deleteCouponById(
        @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(null);
    }
}
