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
import vn.clothing.fashion_shop.mapper.OptionMapper;
import vn.clothing.fashion_shop.repository.OptionRepository;
import vn.clothing.fashion_shop.service.OptionService;
import vn.clothing.fashion_shop.service.OptionValueService;
import vn.clothing.fashion_shop.web.rest.DTO.responses.OptionResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OptionServiceImpl implements OptionService {
    private final OptionRepository optionRepository;
    private final OptionMapper optionMapper;

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public OptionResponse createOption(Option option){
        log.info("[createOption] start create option ....");
        try {
            final String slug = SlugUtil.toSlug(option.getName());
            if(getRawOptionBySlug(slug,null) != null){
                throw new ServiceException(EnumError.OPTION_DATA_EXISTED_NAME, "option.exist.name",Map.of("name", option.getName()));
            }
            final Option createOption = Option.builder()
            .name(option.getName())
            .slug(slug)
            .activated(true)
            .build();
            return optionMapper.toDto(this.optionRepository.saveAndFlush(createOption));   
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[createOption] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public OptionResponse updateOption(Option option){
        log.info("[updateOption] start update option ....");
        try {
            Option updateOption = getRawOptionById(option.getId());
            if(updateOption == null){
                throw new ServiceException(EnumError.OPTION_ERR_NOT_FOUND_ID, "option.not.found.id",Map.of("id", option.getId()));
            }
            String slug = SlugUtil.toSlug(option.getName());
            if(getRawOptionBySlug(slug,option.getId()) != null){
                throw new ServiceException(EnumError.OPTION_DATA_EXISTED_NAME, "option.exist.name",Map.of("name", option.getName()));
            }
            updateOption.setSlug(slug);
            updateOption.setName(option.getName());
            updateOption = this.optionRepository.saveAndFlush(updateOption);
            return optionMapper.toDto(updateOption);   
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[updateOption] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    private Option getRawOptionBySlug(String slug, Long checkId){
        try {
            Optional<Option> opValue = checkId == null ? 
                this.optionRepository.findBySlug(slug) :
                this.optionRepository.findBySlugAndIdNot(slug,checkId) 
            ;
            return opValue.isPresent() ? opValue.get() : null;
            
        } catch (Exception e) {
            log.error("[getRawOptionBySlug] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Option getRawOptionById(Long id){
        try {
            Optional<Option> opValue = this.optionRepository.findById(id);
            return opValue.isPresent() ? opValue.get() : null;
        } catch (Exception e) {
            log.error("[getRawOptionById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OptionResponse getOptionById(Long id){
        try {
            Option option = getRawOptionById(id);
            if(option == null){
                throw new ServiceException(EnumError.OPTION_ERR_NOT_FOUND_ID, "option.not.found.id",Map.of("id", id));
            }
            return optionMapper.toDto(option);   
        } catch (ServiceException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("[getOptionById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse getAllOption(Pageable pageable,Specification specification){
        try {
            final Page<Option> options = this.optionRepository.findAll(specification, pageable);
            final List<OptionResponse> optionDTOs = this.optionMapper.toDto(options.getContent());
            return ConvertPagination.handleConvert(pageable, options, optionDTOs);
        } catch (Exception e) {
            log.error("[getAllOption] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
}
