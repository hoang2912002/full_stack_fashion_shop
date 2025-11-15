package vn.clothing.fashion_shop.service;

import java.util.List;
import java.util.Set;

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
    List<Category> findListCategoryById(List<Long> ids);
    Category findRawCategoryById(Long id);
    List<Category> findRawAllCategory();
    List<Category> getCategoryTreeStartByListId(List<Long> ids);
    boolean isLeaf(Category category);
    // Set<Category> getAllChildCategories(Category category, Long id);
}
