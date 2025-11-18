package vn.clothing.fashion_shop.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;

import vn.clothing.fashion_shop.domain.Product;
import vn.clothing.fashion_shop.web.rest.DTO.requests.VariantRequest.InnerVariantRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ProductResponse;

public interface ProductService {
    ProductResponse createProduct(Product product, List<InnerVariantRequest> variants);
    ProductResponse updateProduct(Product product, List<InnerVariantRequest> variants);
    PaginationResponse getAllProduct(Pageable pageable, Specification spec);
    ProductResponse getProductById(Long id);
    List<Product> findListProductById(List<Long> ids);
    Product lockProductById(Long id);
}
