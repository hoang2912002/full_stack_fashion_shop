package vn.clothing.fashion_shop.service.impls;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.constants.enumEntity.PromotionEnum;
import vn.clothing.fashion_shop.constants.util.ConvertPagination;
import vn.clothing.fashion_shop.domain.Category;
import vn.clothing.fashion_shop.domain.Product;
import vn.clothing.fashion_shop.domain.Promotion;
import vn.clothing.fashion_shop.domain.PromotionProduct;
import vn.clothing.fashion_shop.domain.Role;
import vn.clothing.fashion_shop.mapper.CategoryMapper;
import vn.clothing.fashion_shop.mapper.ProductMapper;
import vn.clothing.fashion_shop.mapper.PromotionMapper;
import vn.clothing.fashion_shop.repository.PromotionProductRepository;
import vn.clothing.fashion_shop.repository.PromotionRepository;
import vn.clothing.fashion_shop.service.CategoryService;
import vn.clothing.fashion_shop.service.ProductService;
import vn.clothing.fashion_shop.service.PromotionService;
import vn.clothing.fashion_shop.web.rest.DTO.requests.CategoryRequest.InnerCategoryRequest;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ProductRequest.InnerProductRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PromotionResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.RoleResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.CategoryResponse.InnerCategoryResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ProductResponse.InnerProductResponse;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromotionServiceImpl implements PromotionService {
    
    private final PromotionRepository promotionRepository;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;
    private final PromotionMapper promotionMapper;
    private final PromotionProductRepository promotionProductRepository;
    
    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public PromotionResponse createPromotion(Promotion promotion,List<InnerCategoryRequest> categoryRequests, List<InnerProductRequest> productRequests) {
        try {
            return upSertPromotion(promotion, categoryRequests, productRequests);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[createPromotion] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public PromotionResponse updatePromotion(Promotion promotion, List<InnerCategoryRequest> categoryRequests, List<InnerProductRequest> productRequests) {
        try {
            if(getRawPromotionById(promotion.getId()) == null){
                throw new ServiceException(
                    EnumError.PROMOTION_ERR_NOT_FOUND_ID, 
                    "promotion.not.found.id",
                    Map.of("id", promotion.getId()));
            }
            return upSertPromotion(promotion, categoryRequests, productRequests);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[updatePromotion] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PromotionResponse getPromotionById(Long id) {
        try {
            Promotion promotion = this.getRawPromotionById(id);
            if(promotion == null){
                throw new ServiceException(
                    EnumError.PROMOTION_ERR_NOT_FOUND_ID, 
                    "promotion.not.found.id",
                    Map.of("id", id));
            }
            final List<InnerCategoryResponse> pCategories = promotion.getPromotionProducts().stream()
                .map(PromotionProduct::getCategory)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Category::getId, c -> categoryMapper.toMiniDto(c), (a, b) -> a))
                .values()
                .stream()
                .toList();

            final List<InnerProductResponse> pProducts = promotion.getPromotionProducts().stream()
                .map(PromotionProduct::getProduct)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Product::getId, p -> productMapper.toMinDto(p), (a, b) -> a))
                .values()
                .stream()
                .toList();
            final PromotionResponse promotionDTO = promotionMapper.detailDto(promotion);
            promotionDTO.setCategories(pCategories);
            promotionDTO.setProducts(pProducts);
            return promotionDTO;
        } catch(ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[getPromotionById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse getAllPromotion(Pageable pageable, Specification spec) {
        try {
            Page<Promotion> promotions = this.promotionRepository.findAll(spec, pageable);
            List<PromotionResponse> promotionDTOS = promotionMapper.toDto(promotions.getContent());
            return ConvertPagination.handleConvert(pageable, promotions, promotionDTOS);
        } catch (Exception e) {
            log.error("[getAllPromotion] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
    
    private Promotion getRawPromotionByCode(String code, Long checkId){
        Optional<Promotion> promotion = checkId != null ? this.promotionRepository.findByCodeAndIdNot(code,checkId) : this.promotionRepository.findByCode(code);
        return promotion.isPresent() ? promotion.get() : null;
    }
    
    private Promotion getRawPromotionById(Long id){
        Optional<Promotion> promotion = this.promotionRepository.findById(id);
        return promotion.isPresent() ? promotion.get() : null;
    }

    @Transactional(rollbackFor = ServiceException.class)
    private PromotionResponse upSertPromotion(Promotion promotion, List<InnerCategoryRequest> categoryRequests, List<InnerProductRequest> productRequests){
        try {

            if(getRawPromotionByCode(promotion.getCode(), promotion.getId()) != null){
                throw new ServiceException(
                    EnumError.PROMOTION_DATA_EXISTED_CODE, 
                    "promotion.exist.code",
                    Map.of("code", promotion.getCode()));
            }
            
            final boolean isPercentType = promotion.getOptionPromotion() == 0;
            final boolean isAmountType = promotion.getOptionPromotion() == 1;
            final Promotion createPromotion = Promotion.builder()
                .id(promotion.getId())
                .name(promotion.getName())
                .code(promotion.getCode())
                .description(promotion.getDescription())
                .discountPercent(isPercentType ? promotion.getDiscountPercent() : null)
                .minDiscountAmount(isAmountType ? promotion.getMinDiscountAmount() : null)
                .maxDiscountAmount(isAmountType ? promotion.getMaxDiscountAmount() : null)
                .quantity(promotion.getQuantity())
                .discountType(promotion.getDiscountType())
                .startDate(promotion.getStartDate())
                .endDate(promotion.getEndDate())
                .activated(true)
                .optionPromotion(promotion.getOptionPromotion())
            .build();

            final Promotion promotionCreated = this.promotionRepository.saveAndFlush(createPromotion);

            if ((categoryRequests == null || categoryRequests.isEmpty()) &&
                (productRequests == null || productRequests.isEmpty())) {
                return promotionMapper.toDto(promotionCreated);
            }
            List<Long> categoryIds = categoryRequests == null ? List.of() :
                categoryRequests.stream()
                        .filter(Objects::nonNull)
                        .map(InnerCategoryRequest::getId)
                        .distinct()
                        .toList();

            List<Long> productIds = productRequests == null ? List.of() :
                productRequests.stream()
                        .filter(Objects::nonNull)
                        .map(InnerProductRequest::getId)
                        .distinct()
                        .toList();
            
            List<Category> allCategories = this.categoryService.getCategoryTreeStartByListId(categoryIds);
            
            List<Product> products = productService.findListProductById(productIds);
            List<PromotionProduct> promotionProducts = new ArrayList<>();

            if(promotionCreated.getDiscountType().equals(PromotionEnum.CATEGORY)){
                promotionProducts = allCategories.stream()
                .flatMap(c -> c.getProducts().stream()
                    .map(p -> PromotionProduct.builder()
                        .category(c)
                        .product(p)
                        .promotion(promotionCreated)
                        .activated(true)
                        .build()
                    )
                )
                .collect(Collectors.toList());
            }
            else{
                promotionProducts = products.stream()
                    .map(product -> PromotionProduct.builder()
                            .category(product.getCategory())
                            .product(product)
                            .promotion(promotionCreated)
                            .build()
                    )
                    .toList();
            }
            final List<PromotionProduct> createPromotionProducts = this.promotionProductRepository.saveAllAndFlush(promotionProducts);
            
            final List<InnerCategoryResponse> pCategories = createPromotionProducts.stream()
                .map(pp -> pp.getCategory())
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Category::getId, c -> categoryMapper.toMiniDto(c), (a, b) -> a))
                .values()
                .stream()
                .toList();

            final List<InnerProductResponse> pProducts = createPromotionProducts.stream()
                .map(pp -> pp.getProduct())
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Product::getId, p -> productMapper.toMinDto(p), (a, b) -> a))
                .values()
                .stream()
                .toList();
            final PromotionResponse promotionDTO = promotionMapper.toDto(promotionCreated);

            promotionDTO.setCategories(pCategories);
            promotionDTO.setProducts(pProducts);
            return promotionDTO;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[createPromotion] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
}
