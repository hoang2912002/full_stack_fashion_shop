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

import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.domain.Promotion;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;




@RestController
@RequestMapping("/api/v1/admin/promotions")
public class PromotionController {

    @PostMapping("")
    @ApiMessageResponse("promotion.success.create")
    public ResponseEntity<Promotion> createPromotion(
        @RequestBody Promotion promotion
    ) {

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    

    @GetMapping("/{id}")
    @ApiMessageResponse("promotion.success.update")
    public ResponseEntity<Promotion> getPromotionById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping("")
    @ApiMessageResponse("promotion.success.get.single")
    public ResponseEntity<Promotion> updatePromotionById(
        @RequestBody Promotion promotion
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
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
