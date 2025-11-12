package vn.clothing.fashion_shop.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import vn.clothing.fashion_shop.domain.Option;
import vn.clothing.fashion_shop.web.rest.DTO.responses.OptionResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;

public interface OptionService {
    public OptionResponse createOption(Option option);

    public OptionResponse updateOption(Option option);

    // public Option getRawOptionBySlug(String slug, Long checkId);

    public Option getRawOptionById(Long id);

    public OptionResponse getOptionById(Long id);

    public PaginationResponse getAllOption(Pageable pageable,Specification specification);
}
