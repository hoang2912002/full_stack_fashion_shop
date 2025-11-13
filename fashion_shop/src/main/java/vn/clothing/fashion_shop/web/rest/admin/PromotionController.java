package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import vn.clothing.fashion_shop.domain.Promotion;
import vn.clothing.fashion_shop.mapper.PromotionMapper;
import vn.clothing.fashion_shop.service.PromotionService;
import vn.clothing.fashion_shop.web.rest.DTO.requests.PromotionRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PromotionResponse;




@RestController
@RequestMapping("/api/v1/admin/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;
    private final PromotionMapper promotionMapper;

    @PostMapping("")
    @ApiMessageResponse("promotion.success.create")
    public ResponseEntity<PromotionResponse> createPromotion(
        @RequestBody @Valid PromotionRequest promotion
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            this.promotionService.createPromotion(
                promotionMapper.toValidator(promotion),promotion.getCategories(),promotion.getProducts()
            )
        );
    }
    
    @PutMapping("")
    @ApiMessageResponse("promotion.success.update")
    public ResponseEntity<PromotionResponse> updatePromotionById(
        @RequestBody @Valid PromotionRequest promotion
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.promotionService.updatePromotion(
            promotionMapper.toValidator(promotion),promotion.getCategories(),promotion.getProducts()
        ));
    }

    @GetMapping("/{id}")
    @ApiMessageResponse("promotion.success.get.single")
    public ResponseEntity<Promotion> getPromotionById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.promotionService.getPromotionById(null));
    }
    
    @GetMapping("")
    @ApiMessageResponse("promotion.success.get.all")
    public ResponseEntity<PaginationResponse> getAllPromotion(
        Pageable pageable,
        @Filter Specification<Promotion> spec
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    
}
