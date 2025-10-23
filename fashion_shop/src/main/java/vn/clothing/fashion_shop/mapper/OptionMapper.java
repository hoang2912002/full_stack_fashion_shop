package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.Option;
import vn.clothing.fashion_shop.web.rest.DTO.option.CreateOptionDTO;
import vn.clothing.fashion_shop.web.rest.DTO.option.GetOptionDTO;
import vn.clothing.fashion_shop.web.rest.DTO.option.UpdateOptionDTO;


@Mapper(
    componentModel = "spring",
    uses = {
        OptionValueMapper.class
    }
)
public interface OptionMapper {
    OptionMapper INSTANCE = Mappers.getMapper(OptionMapper.class);

    CreateOptionDTO toCreate(Option option);
    UpdateOptionDTO toUpdate(Option option);
    GetOptionDTO toGetDto(Option option);

    List<GetOptionDTO> toGetListDTO(List<Option> options);

    Option toEntity(CreateOptionDTO dto);
    Option toEntity(UpdateOptionDTO dto);
    Option toEntity(GetOptionDTO dto);
}
