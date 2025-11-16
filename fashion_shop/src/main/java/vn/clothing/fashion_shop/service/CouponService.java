package vn.clothing.fashion_shop.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import vn.clothing.fashion_shop.domain.Coupon;
import vn.clothing.fashion_shop.web.rest.DTO.responses.CouponResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;

public interface CouponService {
    CouponResponse createCoupon(Coupon coupon);
    CouponResponse updateCoupon(Coupon coupon);
    CouponResponse getCouponById(Long id);
    PaginationResponse getAllCoupon(Pageable pageable, Specification specification);
    Void deleteCouponById(Long id);
}
