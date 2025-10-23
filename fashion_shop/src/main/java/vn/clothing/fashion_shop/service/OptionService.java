package vn.clothing.fashion_shop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.clothing.fashion_shop.constants.util.ConvertPagination;
import vn.clothing.fashion_shop.constants.util.SlugUtil;
import vn.clothing.fashion_shop.domain.Option;
import vn.clothing.fashion_shop.domain.OptionValue;
import vn.clothing.fashion_shop.mapper.OptionMapper;
import vn.clothing.fashion_shop.repository.OptionRepository;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.option.GetOptionDTO;

@Service
public class OptionService {
    private final OptionRepository optionRepository;
    private final OptionMapper optionMapper;
    private final OptionValueService optionValueService;

    public OptionService(OptionRepository optionRepository,OptionMapper optionMapper,OptionValueService optionValueService) {
        this.optionRepository = optionRepository;
        this.optionMapper = optionMapper;
        this.optionValueService = optionValueService;
    }
    
    public GetOptionDTO createOption(Option option){
        String slug = SlugUtil.toSlug(option.getName());
        if(getRawOptionBySlug(slug,null) != null){
            throw new RuntimeException("Option: " + option.getName() + " đã tồn tại");
        }
        Option createOption = Option.builder()
        .name(option.getName())
        .slug(slug)
        .activated(true)
        .build();
        createOption = this.optionRepository.saveAndFlush(createOption);
        return optionMapper.toDto(createOption);
    }

    public GetOptionDTO updateOption(Option option){
        Option updateOption = getRawOptionById(option.getId());
        if(updateOption == null){
            throw new RuntimeException("Option với id: " + option.getId() + " không tồn tại");
        }
        String slug = SlugUtil.toSlug(option.getName());
        if(getRawOptionBySlug(slug,option.getId()) != null){
            throw new RuntimeException("Option: " + option.getName() + " đã tồn tại");
        }
        updateOption.setSlug(slug);
        updateOption.setName(option.getName());
        updateOption = this.optionRepository.saveAndFlush(updateOption);
        return optionMapper.toDto(updateOption);
    }

    public Option getRawOptionBySlug(String slug, Long checkId){
        Optional<Option> opValue = checkId == null ? 
            this.optionRepository.findBySlug(slug) :
            this.optionRepository.findBySlugAndIdNot(slug,checkId) 
        ;
        return opValue.isPresent() ? opValue.get() : null;
    }

    public Option getRawOptionById(Long id){
        Optional<Option> opValue = this.optionRepository.findById(id);
        return opValue.isPresent() ? opValue.get() : null;
    }

    public GetOptionDTO getOptionById(Long id){
        Option option = getRawOptionById(id);
        if(option == null){
            throw new RuntimeException("Option với id: " + id + " không tồn tại");
        }
        return optionMapper.toDto(option);
    }

    public PaginationDTO getAllOption(Pageable pageable,Specification specification){
        Page<Option> options = this.optionRepository.findAll(specification, pageable);
        List<GetOptionDTO> optionDTOs = this.optionMapper.toDto(options.getContent());
        return ConvertPagination.handleConvert(pageable, options, optionDTOs);
    }
}
