package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.domain.Category;
import vn.clothing.fashion_shop.mapper.CategoryMapper;
import vn.clothing.fashion_shop.service.CategoryService;
import vn.clothing.fashion_shop.web.rest.DTO.requests.CategoryRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.CategoryResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;

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
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    
    @PostMapping("")
    @ApiMessageResponse("category.success.create")
    public ResponseEntity<CategoryResponse> createCategory(
        @RequestBody @Valid CategoryRequest category
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.categoryService.createCategory(categoryMapper.toValidator(category)));
    }

    @PutMapping("")
    @ApiMessageResponse("category.success.update")
    public ResponseEntity<CategoryResponse> updateCategory(
        @RequestBody @Valid CategoryRequest category
    ) {        
        Category updateCategory = new Category();
        BeanUtils.copyProperties(category, updateCategory);
        return ResponseEntity.ok(this.categoryService.updateCategory(updateCategory));
    }
    
    @GetMapping("/{id}")
    @ApiMessageResponse("category.success.get.single")
    public ResponseEntity<CategoryResponse> getCategoryById(
        @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(this.categoryService.getCategoryById(id));
    }

    @GetMapping("")
    @ApiMessageResponse("category.success.get.all")
    public ResponseEntity<PaginationResponse> getAllCategory(
        Pageable pageable,
        @Filter Specification spec
    ) {
        return ResponseEntity.ok(this.categoryService.getAllCategory(spec,pageable));
    }
    
    @DeleteMapping("/{id}")
    @ApiMessageResponse("category.success.delete")
    public ResponseEntity<Void> deleteCategoryById(
        @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(null);
    }
    
}
