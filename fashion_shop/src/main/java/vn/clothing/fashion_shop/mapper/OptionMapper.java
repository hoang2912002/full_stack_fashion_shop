package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.Option;
import vn.clothing.fashion_shop.web.rest.DTO.option.GetOptionDTO;
import vn.clothing.fashion_shop.web.rest.DTO.option.GetOptionDTO.InnerOptionDTO;


@Mapper(
    componentModel = "spring",
    uses = {
        OptionValueMapper.class
    }
)
public interface OptionMapper extends EntityMapper<GetOptionDTO,Option> {
    OptionMapper INSTANCE = Mappers.getMapper(OptionMapper.class);

    @Named("toDto")
    GetOptionDTO toDto(Option option);

    @IterableMapping(qualifiedByName = "toDto")
    List<GetOptionDTO> toDto(List<Option> options);

    @Named("toMiniDto")
    InnerOptionDTO toMiniDto(Option option);

    @Named("toMiniDto")
    @IterableMapping(qualifiedByName = "toMiniDto")
    List<InnerOptionDTO> toMiniDto(List<Option> option);

    Option toEntity(GetOptionDTO dto);
}
