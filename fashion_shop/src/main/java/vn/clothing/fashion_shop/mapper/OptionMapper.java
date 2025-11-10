package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.Option;
import vn.clothing.fashion_shop.web.rest.DTO.requests.OptionRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.OptionResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.OptionResponse.InnerOptionResponse;


@Mapper(
    componentModel = "spring",
    uses = {
        OptionValueMapper.class
    }
)
public interface OptionMapper extends EntityMapper<OptionResponse,Option> {
    OptionMapper INSTANCE = Mappers.getMapper(OptionMapper.class);

    @Named("toDto")
    OptionResponse toDto(Option option);

    @IterableMapping(qualifiedByName = "toDto")
    List<OptionResponse> toDto(List<Option> options);

    @Named("toMiniDto")
    InnerOptionResponse toMiniDto(Option option);

    @Named("toMiniDto")
    @IterableMapping(qualifiedByName = "toMiniDto")
    List<InnerOptionResponse> toMiniDto(List<Option> option);
    
    Option toValidator(OptionRequest dto);
    Option toEntity(OptionResponse dto);
}
