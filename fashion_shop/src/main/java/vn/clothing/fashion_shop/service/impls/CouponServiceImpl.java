package vn.clothing.fashion_shop.service.impls;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.constants.util.ConvertPagination;
import vn.clothing.fashion_shop.domain.Coupon;
import vn.clothing.fashion_shop.mapper.CouponMapper;
import vn.clothing.fashion_shop.repository.CouponRepository;
import vn.clothing.fashion_shop.service.CouponService;
import vn.clothing.fashion_shop.web.rest.DTO.responses.CouponResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;
    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public CouponResponse createCoupon(Coupon coupon) {
        log.info("[createCoupon] start create coupon ....");
        try {
            return upSert(coupon);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[createCoupon] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public CouponResponse updateCoupon(Coupon coupon) {
        log.info("[updateCoupon] start update coupon ....");
        try {
            if(findRawCouponById(coupon.getId()) == null){
                throw new ServiceException(EnumError.COUPON_ERR_NOT_FOUND_ID, "coupon.not.found.id",Map.of("id", coupon.getId()));
            }
            return upSert(coupon); 
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[updateCoupon] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = ServiceException.class)
    public CouponResponse getCouponById(Long id) {
        try {
            Coupon coupon = findRawCouponById(id);
            if(coupon == null){
                throw new ServiceException(EnumError.COUPON_ERR_NOT_FOUND_ID, "coupon.not.found.id",Map.of("id", id));  
            }
            return couponMapper.toDto(coupon);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[updateCoupon] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = ServiceException.class)
    public PaginationResponse getAllCoupon(Pageable pageable, Specification specification) {
        try {
            final Page<Coupon> coupons = this.couponRepository.findAll(specification, pageable);
            final List<CouponResponse> couponsList = couponMapper.toDto(coupons.getContent());
            return ConvertPagination.handleConvert(pageable, coupons, couponsList);
        } catch (Exception e) {
            log.error("[getAllCoupon] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    public Void deleteCouponById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteCouponById'");
    }
    

    private Coupon findRawCouponByCode(String code, Long checkId){
        Optional<Coupon> cOptional = checkId == null ? this.couponRepository.findByCode(code) : this.couponRepository.findByCodeAndIdNot(code, checkId);
        return cOptional.isPresent() ? cOptional.get() : null;
    }
    
    private Coupon findRawCouponById(Long id){
        Optional<Coupon> cOptional = this.couponRepository.findById(id);
        return cOptional.isPresent() ? cOptional.get() : null;
    }

    private CouponResponse upSert(Coupon coupon){
        try {
            if(findRawCouponByCode(coupon.getCode(),coupon.getId()) != null){
                throw new ServiceException(EnumError.COUPON_DATA_EXISTED_CODE, "coupon.exist.code",Map.of("code", coupon.getCode()));
            }
            Coupon writeCoupon = Coupon.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .name(coupon.getName())
                .couponAmount(coupon.getCouponAmount())
                .stock(coupon.getStock())
                .type(coupon.getType())
                .startDate(coupon.getStartDate())
                .endDate(coupon.getEndDate())
                .activated(true)
            .build();
            return couponMapper.toDto(this.couponRepository.saveAndFlush(writeCoupon));
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[createCoupon] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
}
