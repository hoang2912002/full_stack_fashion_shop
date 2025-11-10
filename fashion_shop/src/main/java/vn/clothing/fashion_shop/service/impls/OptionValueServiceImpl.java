package vn.clothing.fashion_shop.service.impls;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.constants.util.ConvertPagination;
import vn.clothing.fashion_shop.constants.util.SlugUtil;
import vn.clothing.fashion_shop.domain.Option;
import vn.clothing.fashion_shop.domain.OptionValue;
import vn.clothing.fashion_shop.mapper.OptionValueMapper;
import vn.clothing.fashion_shop.repository.OptionRepository;
import vn.clothing.fashion_shop.repository.OptionValueRepository;
import vn.clothing.fashion_shop.service.OptionService;
import vn.clothing.fashion_shop.service.OptionValueService;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.responses.OptionValueResponse;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OptionValueServiceImpl implements OptionValueService {
    private final OptionValueRepository optionValueRepository;
    private final OptionValueMapper optionValueMapper;
    private final OptionRepository optionRepository;
    private final OptionService optionService;

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public OptionValueResponse createOptionValue(OptionValue optionValue){
        log.info("[createOptionValue] start create option value ....");
        try {
            final String slug =  SlugUtil.toSlug(optionValue.getValue());
            if(getRawOptionValueBySlug(slug,null) != null){
                throw new ServiceException(EnumError.OPTION_VALUE_DATA_EXISTED_VALUE, "option.value.exist.value",Map.of("value", optionValue.getValue()));
            }
            
            Option option = new Option();
            if(optionValue.getOption().getId() != null){
                option = this.optionService.getRawOptionById(optionValue.getOption().getId());
            }
            final OptionValue createOptionValue = OptionValue.builder()
                .value(optionValue.getValue())
                .slug(slug)
                .activated(true)
                .option(option)
                .build();
            return optionValueMapper.toDto(this.optionValueRepository.saveAndFlush(createOptionValue));
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[createOptionValue] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public OptionValueResponse updateOptionValue(OptionValue optionValue){
        log.info("[updateOptionValue] start update option value ....");
        try {
            final OptionValue updateOptionValue = getRawOptionValueById(optionValue.getId());
            if(updateOptionValue == null){
                throw new ServiceException(EnumError.OPTION_VALUE_ERR_NOT_FOUND_ID, "option.value.not.found.id",Map.of("id", optionValue.getId()));
            }
            final String slug =  SlugUtil.toSlug(optionValue.getValue());
            if(getRawOptionValueBySlug(slug,optionValue.getId()) != null){
                throw new ServiceException(EnumError.OPTION_VALUE_DATA_EXISTED_VALUE, "option.value.exist.value",Map.of("value", optionValue.getValue()));
            }
            Option option = new Option();
            if(optionValue.getOption().getId() != null){
                option = this.optionService.getRawOptionById(optionValue.getOption().getId());
            }
            updateOptionValue.setValue(optionValue.getValue());
            updateOptionValue.setSlug(slug);
            updateOptionValue.setOption(option);
            return optionValueMapper.toDto(this.optionValueRepository.saveAndFlush(updateOptionValue));
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[updateOptionValue] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    // public Option getRawOptionById(Long id){
    //     Optional<Option> opValue = this.optionRepository.findById(id);;
    //     return opValue.isPresent() ? opValue.get() : null;
    // }
    
    @Override
    @Transactional(readOnly = true)
    public OptionValue getRawOptionValueBySlug(String slug, Long checkId){
        try {
            Optional<OptionValue> opValue = checkId == null ? 
            this.optionValueRepository.findBySlug(slug) :
            this.optionValueRepository.findBySlugAndIdNot(slug,checkId);
            return opValue.isPresent() ? opValue.get() : null;   
        } catch (Exception e) {
            log.error("[getRawOptionValueBySlug] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionValue> getRawListOptionValueBySlug(List<String> slugs){
        try {
            return this.optionValueRepository.findAllBySlugIn(slugs);
        } catch (Exception e) {
            log.error("[getRawListOptionValueBySlug] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OptionValue getRawOptionValueById(Long id){
        try {
            Optional<OptionValue> opValue = this.optionValueRepository.findById(id);
            return opValue.isPresent() ? opValue.get() : null;
        } catch (Exception e) {
            log.error("[getRawOptionValueById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionValue> getRawListOptionValueById(List<Long> id){
        try {
            return this.optionValueRepository.findAllByIdIn(id);
            
        } catch (Exception e) {
            log.error("[getRawListOptionValueById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OptionValueResponse getOptionValueById(Long id){
        try {
            OptionValue optionValue = getRawOptionValueById(id);
            if(optionValue == null){
                throw new ServiceException(EnumError.OPTION_VALUE_ERR_NOT_FOUND_ID, "option.value.not.found.id",Map.of("id", id));
            }
            return optionValueMapper.toDetailDto(optionValue);
        } catch (Exception e) {
            log.error("[getOptionValueById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationDTO getAllOptionValue(Pageable pageable, Specification specification){
        try {
            Page<OptionValue> optionValues = this.optionValueRepository.findAll(specification, pageable);
            List<OptionValueResponse> listOpValue = optionValueMapper.toDto(optionValues.getContent());
            return ConvertPagination.handleConvert(pageable, optionValues, listOpValue);
        } catch (Exception e) {
            log.error("[getOptionValueById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
}
