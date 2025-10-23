package vn.clothing.fashion_shop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.clothing.fashion_shop.constants.util.ConvertPagination;
import vn.clothing.fashion_shop.constants.util.SlugUtil;
import vn.clothing.fashion_shop.domain.OptionValue;
import vn.clothing.fashion_shop.mapper.OptionValueMapper;
import vn.clothing.fashion_shop.repository.OptionValueRepository;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.optionValue.CreateOptionValueDTO;
import vn.clothing.fashion_shop.web.rest.DTO.optionValue.GetOptionValueDTO;
import vn.clothing.fashion_shop.web.rest.DTO.optionValue.UpdateOptionValueDTO;

@Service
public class OptionValueService {
    private final OptionValueRepository optionValueRepository;
    private final OptionValueMapper optionValueMapper;

    public OptionValueService(OptionValueRepository optionValueRepository,OptionValueMapper optionValueMapper) {
        this.optionValueRepository = optionValueRepository;
        this.optionValueMapper = optionValueMapper;
    }

    public CreateOptionValueDTO createOptionValue(OptionValue optionValue){
        String slug =  SlugUtil.toSlug(optionValue.getValue());
        if(getRawOptionValueBySlug(slug,null) != null){
            throw new RuntimeException("Gía trị: " + optionValue.getValue() + " đã tồn tại");
        }
        OptionValue createOptionValue = OptionValue.builder()
        .value(optionValue.getValue())
        .slug(slug)
        .activated(true)
        .build();
        createOptionValue = this.optionValueRepository.saveAndFlush(createOptionValue);
        return optionValueMapper.toCreateDto(createOptionValue);
    }

    public UpdateOptionValueDTO updateOptionValue(OptionValue optionValue){
        OptionValue updateOptionValue = getRawOptionValueById(optionValue.getId());
        if(updateOptionValue == null){
            throw new RuntimeException("Gía trị với id: " + optionValue.getId() + " không tồn tại");
        }
        String slug =  SlugUtil.toSlug(optionValue.getValue());
        if(getRawOptionValueBySlug(slug,optionValue.getId()) != null){
            throw new RuntimeException("Gía trị: " + optionValue.getValue() + " đã tồn tại");
        }
        updateOptionValue.setValue(optionValue.getValue());
        updateOptionValue.setSlug(slug);
        updateOptionValue = this.optionValueRepository.saveAndFlush(updateOptionValue);
        return optionValueMapper.toUpdateDto(updateOptionValue);
    }

    public OptionValue getRawOptionValueBySlug(String slug, Long checkId){
        Optional<OptionValue> opValue = checkId == null ? 
            this.optionValueRepository.findBySlug(slug) :
            this.optionValueRepository.findBySlugAndIdNot(slug,checkId) 
        ;
        return opValue.isPresent() ? opValue.get() : null;
    }

    public OptionValue getRawOptionValueById(Long id){
        Optional<OptionValue> opValue = this.optionValueRepository.findById(id);
        return opValue.isPresent() ? opValue.get() : null;
    }
    public List<OptionValue> getRawListOptionValueById(List<Long> id){
        return this.optionValueRepository.findAllByIdIn(id);
    }

    public GetOptionValueDTO getOptionValueById(Long id){
        OptionValue optionValue = getRawOptionValueById(id);
        if(optionValue == null){
            throw new RuntimeException("Gía trị với id: " + id + " không tồn tại");
        }
        return optionValueMapper.toGetDto(optionValue);
    }

    public PaginationDTO getAllOptionValue(Pageable pageable, Specification specification){
        Page<OptionValue> optionValues = this.optionValueRepository.findAll(specification, pageable);
        List<GetOptionValueDTO> listOpValue = optionValueMapper.toGetListDTO(optionValues.getContent());
        return ConvertPagination.handleConvert(pageable, optionValues, listOpValue);
    }
}
