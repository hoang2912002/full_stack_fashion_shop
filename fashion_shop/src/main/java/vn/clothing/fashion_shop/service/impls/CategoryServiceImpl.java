package vn.clothing.fashion_shop.service.impls;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
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
import vn.clothing.fashion_shop.web.rest.DTO.requests.CategoryRequest.InnerCategoryRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.CategoryResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;
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
    public PaginationResponse getAllCategory(Specification spec, Pageable pageable){
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
    public List<Category> findListCategoryById(List<Long> id){
        try {
            return this.categoryRepository.findAllByIdIn(id);
        } catch (Exception e) {
            log.error("[findListCategoryById] Error: {}", e.getMessage(), e);
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
    @Transactional(readOnly = true)
    public List<Category> findRawAllCategory(){
        try {
            List<Category> categories = this.categoryRepository.findAll();
            return categories;
        } catch (Exception e) {
            log.error("[findRawAllCategory] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getCategoryTreeStartByListId(List<Long> ids){
        try {
            List<Category> allCategories = categoryRepository.findAll();

            // 2️⃣ Build map id → Category để lookup nhanh
            Map<Long, Category> categoryMap = allCategories.stream()
                    .collect(Collectors.toMap(Category::getId, Function.identity()));

            // 3️⃣ Build map parentId → list childId để traverse cây bằng id
            Map<Long, List<Long>> childrenMapById = allCategories.stream()
                    .filter(c -> c.getParent() != null)
                    .collect(Collectors.groupingBy(
                            c -> c.getParent().getId(),
                            Collectors.mapping(Category::getId, Collectors.toList())
                    ));

            // 4️⃣ Duyệt từng rootId, lấy tất cả các node con (subtree)
            Set<Long> allSubtreeIds = new HashSet<>();
            for (Long rootId : ids) {
                //Dùng stack để thực hiện DFS (Depth-First Search).
                //ArrayDeque với push và pop theo cơ chế LIFO → Last In First Out.
                Deque<Long> stack = new ArrayDeque<>();
                stack.push(rootId);

                while (!stack.isEmpty()) {
                    Long currentId = stack.pop();
                    if (allSubtreeIds.add(currentId)) { // add xong mới push
                        List<Long> childrenIds = childrenMapById.get(currentId);
                        if (childrenIds != null) {
                            stack.addAll(childrenIds);
                        }
                    }
                }
            }

            // 5️⃣ Lọc leaf nodes (không có con)
            List<Category> leafCategories = allSubtreeIds.stream()
                    .map(categoryMap::get)
                    .filter(cat -> !childrenMapById.containsKey(cat.getId()) || childrenMapById.get(cat.getId()).isEmpty())
                    .toList();

            return leafCategories;
        } catch (Exception e) {
            log.error("[getCategoryTreeStartByListId] Error: {}", e.getMessage());
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLeaf(Category category) {
        return category.getChildren() == null || category.getChildren().isEmpty();
    }
}
