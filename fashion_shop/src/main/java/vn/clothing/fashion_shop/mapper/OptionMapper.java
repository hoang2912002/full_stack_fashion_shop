package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.Option;
import vn.clothing.fashion_shop.web.rest.DTO.option.GetOptionDTO;


@Mapper(
    componentModel = "spring",
    uses = {
        OptionValueMapper.class
    }
)
public interface OptionMapper extends EntityMapper<GetOptionDTO,Option> {
    OptionMapper INSTANCE = Mappers.getMapper(OptionMapper.class);

    GetOptionDTO toDto(Option option);

    List<GetOptionDTO> toDto(List<Option> options);

    Option toEntity(GetOptionDTO dto);
}
