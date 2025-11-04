package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.Category;
import vn.clothing.fashion_shop.web.rest.DTO.category.GetCategoryDTO;
import vn.clothing.fashion_shop.web.rest.DTO.category.GetCategoryDTO.InnerGetCategoryDTO;


@Mapper(
    componentModel = "spring"
)
public interface CategoryMapper extends EntityMapper<GetCategoryDTO, Category>  {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Named("toMiniDto")
    InnerGetCategoryDTO toMiniDto(Category category);
    GetCategoryDTO toDto(Category category);
    List<GetCategoryDTO> toDto(List<Category> categories);

    Category toEntity(GetCategoryDTO dto);

}
