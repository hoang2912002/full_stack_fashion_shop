package vn.clothing.fashion_shop.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import vn.clothing.fashion_shop.domain.Category;
import vn.clothing.fashion_shop.web.rest.DTO.responses.CategoryResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;

public interface CategoryService {
    CategoryResponse createCategory(Category category);
    CategoryResponse updateCategory(Category category);
    CategoryResponse getCategoryById(Long id);
    PaginationResponse getAllCategory(Specification spec, Pageable pageable);
    Category findCategoryBySlug(String slug, Long checkId);
    Category findRawCategoryById(Long id);
    boolean isLeaf(Category category);
}
