package vn.clothing.fashion_shop.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.clothing.fashion_shop.constants.util.ConvertPagination;
import vn.clothing.fashion_shop.constants.util.SlugUtil;
import vn.clothing.fashion_shop.domain.Category;
import vn.clothing.fashion_shop.repository.CategoryRepository;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.category.CreateCategoryDTO;
import vn.clothing.fashion_shop.web.rest.DTO.category.GetCategoryDTO;
import vn.clothing.fashion_shop.web.rest.DTO.category.UpdateCategoryDTO;

@Service
public class CategoryService {
    
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    public CreateCategoryDTO createCategory(Category category){
        return upSertCategory(category,null, saved -> {
            CreateCategoryDTO result = new CreateCategoryDTO();
            BeanUtils.copyProperties(saved, result);
            if (saved.getParent() != null) {
                CreateCategoryDTO parentDto = new CreateCategoryDTO();
                BeanUtils.copyProperties(saved.getParent(), parentDto);
                result.setParent(parentDto);
            }
            return result;
        });
    }

    public UpdateCategoryDTO updateCategory(Category category){
        return upSertCategory(category,category.getId(), saved -> {
            UpdateCategoryDTO result = new UpdateCategoryDTO();
            BeanUtils.copyProperties(saved, result);
            if(saved.getParent() != null){
                UpdateCategoryDTO parentDto = new UpdateCategoryDTO();
                BeanUtils.copyProperties(saved.getParent(), parentDto);
                result.setParent(parentDto);
            }
            return result;
        });
    }

    public Category getCategoryById(Long id){
        Category category = findRawCategoryById(id);
        if(category == null){
            throw new RuntimeException("Danh muc với id: " + id + " không tồn tại");
        }
        return category;
    }

    public PaginationDTO getAllCategory(Specification spec, Pageable pageable){
        Page<Category> categories = this.categoryRepository.findAll(spec, pageable);
        List<GetCategoryDTO> categoriesList = categories.getContent().stream()
            .map(c -> GetCategoryDTO.builder()
                .id(c.getId())
                .name(c.getName())
                .slug(c.getSlug())
                .createdBy(c.getCreatedBy())
                .createdAt(c.getCreatedAt())
                .updatedBy(c.getUpdatedBy())
                .updatedAt(c.getUpdatedAt())
                .activated(c.isActivated())
                // .children(
                //     c.getChildren().stream()
                //         .filter(Category::isActivated)
                //         .map(cr -> new GetCategoryDTO.InnerGetCategoryDTO(
                //             cr.getId(),
                //             cr.getName(),
                //             cr.getSlug()
                //         ))
                //         .toList()
                // )
                .build()
            )
            .toList();

        return ConvertPagination.handleConvert(pageable, categories, categoriesList);
    }

    // public <T> T upSertCategory(Category category, Long checkId, Class<T> dtoClass){
    //     String slug = SlugUtil.toSlug(category.getName());
    //     if(findCategoryBySlug(slug, checkId) != null){
    //         throw new RuntimeException("Danh mục sản phẩm: " + slug + " đã tồn tại");
    //     }
    //     Category uSCategory = new Category();
    //     BeanUtils.copyProperties(category, uSCategory);
    //     uSCategory.setSlug(slug);
    //     Category parentCategory = null;
    //     if(category.getParent() != null){
    //         parentCategory = findRawCategoryById(category.getParent().getId());
    //     }
    //     uSCategory.setParent(parentCategory);
    //     uSCategory = this.categoryRepository.save(uSCategory);  
    //     try {
    //         //tạo 1 constructor rỗng
    //         T dto = dtoClass.getDeclaredConstructor().newInstance();
    //         BeanUtils.copyProperties(uSCategory, dto);
    //         if (uSCategory.getParent() != null) {
    //             T parentDTO = dtoClass.getDeclaredConstructor().newInstance();
    //             BeanUtils.copyProperties(uSCategory.getParent(), parentDTO);
    //             // dùng reflection để gọi setParent
    //             dtoClass.getMethod("setParent", dtoClass).invoke(dto, parentDTO);
    //         }
    //         return dto;
    //     } catch (Exception e) {
    //         throw new RuntimeException("Lỗi khi mapping DTO", e);
    //     }
    // }
    
    public <T> T upSertCategory(
        Category category, 
        Long checkId, 
        //Callback function — nhận Category sau khi save, trả về một DTO (T) mà bạn muốn
        Function<Category, T> mapper
    ){
        //Slug
        String slug = SlugUtil.toSlug(category.getName());
        Category existing = findCategoryBySlug(slug,checkId);
        if (existing != null) {
            throw new RuntimeException("Danh mục sản phẩm: " + slug + " đã tồn tại");
        }
        //parent
        if (category.getParent() != null && category.getParent().getId() != null) {
            Category parent = findRawCategoryById(category.getParent().getId());
            category.setParent(parent);
        }
        //save
        Category savedEntity;
        if (checkId != null) {
            existing = new Category();
            BeanUtils.copyProperties(category, existing); // giữ nguyên id
        }
        category.setActivated(true);
        category.setSlug(slug);
        savedEntity = categoryRepository.save(category);
        
        return mapper.apply(savedEntity);
    }
    
    public Category findCategoryBySlug(String slug, Long checkId){
        Optional<Category> cOptional = checkId == null ? this.categoryRepository.findBySlug(slug) : this.categoryRepository.findBySlugAndIdNot(slug,checkId);
        return cOptional.isPresent() && !cOptional.isEmpty() ? cOptional.get() : null;
    }

    public Category findRawCategoryById(Long id){
        Optional<Category> cOptional = this.categoryRepository.findById(id);
        return cOptional.isPresent() && !cOptional.isEmpty() ? cOptional.get() : null;
    }

    public boolean isLeaf(Category category) {
        return category.getChildren() == null || category.getChildren().isEmpty();
    }

}
