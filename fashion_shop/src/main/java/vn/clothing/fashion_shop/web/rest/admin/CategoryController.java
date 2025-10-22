package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.domain.Category;
import vn.clothing.fashion_shop.service.CategoryService;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.category.CreateCategoryDTO;
import vn.clothing.fashion_shop.web.rest.DTO.category.UpdateCategoryDTO;
import vn.clothing.fashion_shop.web.rest.DTO.category.ValidationCategoryDTO;

import org.springframework.beans.BeanUtils;
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
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/v1/admin/categories")
public class CategoryController {
    private final CategoryService categoryService;
    
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("")
    @ApiMessageResponse("Thêm mới danh mục thành công")
    public ResponseEntity<CreateCategoryDTO> createCategory(
        @RequestBody @Valid ValidationCategoryDTO category
    ) {
        Category createCategory = new Category();
        BeanUtils.copyProperties(category, createCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.categoryService.createCategory(createCategory));
    }

    @PutMapping("")
    @ApiMessageResponse("Cập nhật danh mục thành công")
    public ResponseEntity<UpdateCategoryDTO> updateCategory(
        @RequestBody @Valid ValidationCategoryDTO category
    ) {        
        Category updateCategory = new Category();
        BeanUtils.copyProperties(category, updateCategory);
        return ResponseEntity.ok(this.categoryService.updateCategory(updateCategory));
    }
    
    @GetMapping("/{id}")
    @ApiMessageResponse("Lấy danh mục theo id thành công")
    public ResponseEntity<Category> getCategoryById(
        @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(this.categoryService.getCategoryById(id));
    }

    @GetMapping("")
    @ApiMessageResponse("Lấy danh mục sản phẩm thành công")
    public ResponseEntity<PaginationDTO> getAllCategory(
        Pageable pageable,
        @Filter Specification spec
    ) {
        return ResponseEntity.ok(this.categoryService.getAllCategory(spec,pageable));
    }
    
    @DeleteMapping("/{id}")
    @ApiMessageResponse("Xóa danh mục theo id thành công")
    public ResponseEntity<Void> deleteCategoryById(
        @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(null);
    }
    
}
