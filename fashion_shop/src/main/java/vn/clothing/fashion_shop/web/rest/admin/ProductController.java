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
import vn.clothing.fashion_shop.service.ProductService;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.product.GetProductDTO;
import vn.clothing.fashion_shop.web.rest.DTO.product.ValidationProductDTO;

@RestController
@RequestMapping("/api/v1/admin/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("")
    @ApiMessageResponse("Thêm sản phẩm thành công")
    public ResponseEntity<GetProductDTO> createProduct(
        @RequestBody @Valid ValidationProductDTO product
    ) {
        Product createProduct = new Product();
        createProduct = Product.builder()
        .name(product.getName())
        .price(product.getPrice())
        .quantity(product.getQuantity())
        .thumbnail(product.getThumbnail())
        .category(product.getCategory())
        .activated(true)
        .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(this.productService.createProduct(createProduct,product.getVariants()));
    }
    
    @PutMapping("")
    @ApiMessageResponse("Cập nhật sản phẩm thành công")
    public ResponseEntity<Product> updateProduct(
        @RequestBody Product product
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("/{id}")
    @ApiMessageResponse("Lấy sản phẩm theo id thành công")
    public ResponseEntity<Product> getProductById(
        @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("")
    @ApiMessageResponse("Lấy danh sách thành công")
    public ResponseEntity<PaginationDTO> getAllProduct(
        Pageable pageable,
        @Filter Specification<Product> spec
    ){
        return ResponseEntity.ok(null);
    }
    
    @DeleteMapping("/{id}")
    @ApiMessageResponse("Xóa thành công")
    public ResponseEntity<Void> deleteProductById(
        @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(null);
    }
}
