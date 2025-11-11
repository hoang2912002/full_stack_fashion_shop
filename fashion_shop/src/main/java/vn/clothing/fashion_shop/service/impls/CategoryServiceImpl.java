package vn.clothing.fashion_shop.service.impls;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.constants.util.ConvertPagination;
import vn.clothing.fashion_shop.constants.util.SlugUtil;
import vn.clothing.fashion_shop.domain.Category;
import vn.clothing.fashion_shop.mapper.CategoryMapper;
import vn.clothing.fashion_shop.repository.CategoryRepository;
import vn.clothing.fashion_shop.service.CategoryService;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.requests.CategoryRequest.InnerCategoryRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.CategoryResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.CategoryResponse.InnerCategoryResponse;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public CategoryResponse createCategory(Category category){
        log.info("[createCategory] start create category ....");
        try {
            return upSertCategory(category,null, categoryMapper::toDto);
        } catch (Exception e) {
            log.error("[createCategory] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public CategoryResponse updateCategory(Category category){
        log.info("[updateCategory] start update category ....");
        try {
            return upSertCategory(category,category.getId(), saved -> {
                return categoryMapper.toDto(saved);
            });
        } catch (Exception e) {
            log.error("[updateCategory] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class, readOnly = true)
    public CategoryResponse getCategoryById(Long id){
        try {
            Category category = findRawCategoryById(id);
            if(category == null){
                throw new ServiceException(EnumError.CATEGORY_ERR_NOT_FOUND_ID, "category.not.found.id",Map.of("id", id));
            }
            return categoryMapper.toDto(category);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[getCategoryById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
    
    @Override
    @Transactional(rollbackFor = ServiceException.class, readOnly = true)
    public PaginationDTO getAllCategory(Specification spec, Pageable pageable){
        try {
            final Page<Category> categories = this.categoryRepository.findAll(spec, pageable);
            final List<CategoryResponse> categoriesList = categoryMapper.toDto(categories.getContent());
    
            return ConvertPagination.handleConvert(pageable, categories, categoriesList);
        } catch (Exception e) {
            log.error("[getAllCategory] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    private <T> T upSertCategory(
        Category category, 
        Long checkId, 
        //Callback function — nhận Category sau khi save, trả về một DTO (T) mà bạn muốn
        Function<Category, T> mapper
    ){
        try {
            final String slug = SlugUtil.toSlug(category.getName());
            Category existing = findCategoryBySlug(slug,checkId);
            if (existing != null) {
                throw new ServiceException(EnumError.CATEGORY_DATA_EXISTED_SLUG, "category.exist.slug",Map.of("slug", slug));
            }
            //parent
            if (category.getParent() != null && category.getParent().getId() != null) {
                category.setParent(findRawCategoryById(category.getParent().getId()));
            }

            category.setActivated(true);
            category.setSlug(slug);
            return mapper.apply(categoryRepository.save(category));
            
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[upSertCategory] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Category findCategoryBySlug(String slug, Long checkId){
        try {
            Optional<Category> cOptional = checkId == null ? this.categoryRepository.findBySlug(slug) : this.categoryRepository.findBySlugAndIdNot(slug,checkId);
            return cOptional.isPresent() && !cOptional.isEmpty() ? cOptional.get() : null;
        } catch (Exception e) {
            log.error("[findCategoryBySlug] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Category findRawCategoryById(Long id){
        try {
            Optional<Category> cOptional = this.categoryRepository.findById(id);
            return cOptional.isPresent() && !cOptional.isEmpty() ? cOptional.get() : null;
        } catch (Exception e) {
            log.error("[findRawCategoryById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    public boolean isLeaf(Category category) {
        return category.getChildren() == null || category.getChildren().isEmpty();
    }
}
