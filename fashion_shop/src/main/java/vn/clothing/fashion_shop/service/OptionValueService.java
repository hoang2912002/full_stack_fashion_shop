package vn.clothing.fashion_shop.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import vn.clothing.fashion_shop.domain.OptionValue;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.responses.OptionValueResponse;

public interface OptionValueService {
    public OptionValueResponse createOptionValue(OptionValue optionValue);

    public OptionValueResponse updateOptionValue(OptionValue optionValue);

    public OptionValue getRawOptionValueBySlug(String slug, Long checkId);

    public List<OptionValue> getRawListOptionValueBySlug(List<String> slugs);

    public OptionValue getRawOptionValueById(Long id);

    public List<OptionValue> getRawListOptionValueById(List<Long> id);

    public OptionValueResponse getOptionValueById(Long id);

    public PaginationDTO getAllOptionValue(Pageable pageable, Specification specification);
}
