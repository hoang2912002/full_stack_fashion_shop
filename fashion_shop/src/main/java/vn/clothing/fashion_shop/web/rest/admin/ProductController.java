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
import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.domain.Product;
import vn.clothing.fashion_shop.mapper.ProductMapper;
import vn.clothing.fashion_shop.service.ProductService;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ProductRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ProductResponse;

@RestController
@RequestMapping("/api/v1/admin/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService,ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @PostMapping("")
    @ApiMessageResponse("Thêm sản phẩm thành công")
    public ResponseEntity<ProductResponse> createProduct(
        @RequestBody @Valid ProductRequest product
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.productService.createProduct(productMapper.toValidator(product),product.getVariants()));
    }
    
    @PutMapping("")
    @ApiMessageResponse("Cập nhật sản phẩm thành công")
    public ResponseEntity<ProductResponse> updateProduct(
        @RequestBody @Valid ProductRequest product
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.productService.updateProduct(productMapper.toValidator(product),product.getVariants()));
    }

    @GetMapping("/{id}")
    @ApiMessageResponse("Lấy sản phẩm theo id thành công")
    public ResponseEntity<ProductResponse> getProductById(
        @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(this.productService.getProductById(id));
    }

    @GetMapping("")
    @ApiMessageResponse("Lấy danh sách thành công")
    public ResponseEntity<PaginationDTO> getAllProduct(
        Pageable pageable,
        @Filter Specification<Product> spec
    ){
        return ResponseEntity.ok(this.productService.getAllProduct(pageable,spec));
    }
    
    @DeleteMapping("/{id}")
    @ApiMessageResponse("Xóa thành công")
    public ResponseEntity<Void> deleteProductById(
        @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(null);
    }
}
