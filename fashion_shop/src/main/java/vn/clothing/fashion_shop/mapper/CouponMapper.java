package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.Coupon;
import vn.clothing.fashion_shop.web.rest.DTO.requests.CouponRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.CouponResponse.InnerCouponResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.CouponResponse;

@Mapper(
    componentModel = "spring"
)
public interface CouponMapper extends EntityMapper<CouponResponse, Coupon> {
    CouponMapper INSTANCE = Mappers.getMapper(CouponMapper.class);

    @Named("toMiniDto")
    InnerCouponResponse toMiniDto(Coupon coupon);

    @Named("toDto")
    CouponResponse toDto(Coupon coupon);
    List<CouponResponse> toDto(List<Coupon> coupons);

    Coupon toEntity(CouponResponse dto);
    Coupon toValidator(CouponRequest dto);
}
