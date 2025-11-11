package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.Category;
import vn.clothing.fashion_shop.web.rest.DTO.requests.CategoryRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.CategoryResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.CategoryResponse.InnerCategoryResponse;


@Mapper(
    componentModel = "spring"
)
public interface CategoryMapper extends EntityMapper<CategoryResponse, Category>  {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Named("toMiniDto")
    InnerCategoryResponse toMiniDto(Category category);

    @Named("toDto")
    CategoryResponse toDto(Category category);
    List<CategoryResponse> toDto(List<Category> categories);

    Category toEntity(CategoryResponse dto);
    Category toValidator(CategoryRequest dto);
}
