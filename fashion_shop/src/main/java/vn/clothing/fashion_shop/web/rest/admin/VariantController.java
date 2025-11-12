package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.domain.Variant;
import vn.clothing.fashion_shop.service.VariantService;
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
@RequestMapping("/api/v1/admin/variants")
public class VariantController {
    
    private final VariantService variantService;
    
    public VariantController(VariantService variantService) {
        this.variantService = variantService;
    }

    @PostMapping("")
    @ApiMessageResponse("variant.success.create")
    public ResponseEntity<Variant> createVariant(
        @RequestBody Variant variant
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.variantService.createVariant(variant));
    }
    
    @PutMapping("")
    @ApiMessageResponse("variant.success.update")
    public ResponseEntity<Variant> updateVariant(
        @RequestBody Variant variant
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("/{id}")
    @ApiMessageResponse("variant.success.get.single")
    public ResponseEntity<Variant> getVariantById(
        @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("")
    @ApiMessageResponse("variant.success.get.all")
    public ResponseEntity<PaginationResponse> getAllVariant(
        Pageable pageable,
        @Filter Specification<Variant> spec
    ){
        return ResponseEntity.ok(null);
    }
    
    @DeleteMapping("/{id}")
    @ApiMessageResponse("variant.success.delete")
    public ResponseEntity<Void> deleteVariantById(
        @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(null);
    }
}
