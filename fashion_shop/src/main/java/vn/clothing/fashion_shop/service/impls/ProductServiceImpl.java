package vn.clothing.fashion_shop.service.impls;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.constants.util.ConvertPagination;
import vn.clothing.fashion_shop.constants.util.SlugUtil;
import vn.clothing.fashion_shop.domain.Category;
import vn.clothing.fashion_shop.domain.Option;
import vn.clothing.fashion_shop.domain.OptionValue;
import vn.clothing.fashion_shop.domain.Product;
import vn.clothing.fashion_shop.domain.ProductSku;
import vn.clothing.fashion_shop.domain.Variant;
import vn.clothing.fashion_shop.mapper.OptionMapper;
import vn.clothing.fashion_shop.mapper.OptionValueMapper;
import vn.clothing.fashion_shop.mapper.ProductMapper;
import vn.clothing.fashion_shop.repository.ProductRepository;
import vn.clothing.fashion_shop.service.CategoryService;
import vn.clothing.fashion_shop.service.OptionValueService;
import vn.clothing.fashion_shop.service.ProductService;
import vn.clothing.fashion_shop.service.ProductSkuService;
import vn.clothing.fashion_shop.service.VariantService;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.requests.VariantRequest.InnerVariantRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ProductResponse;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductSkuService productSkuService;
    private final VariantService variantService;
    private final ProductMapper productMapper;
    private final OptionMapper optionMapper;
    private final OptionValueMapper optionValueMapper;
    private final OptionValueService optionValueService;

    
    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public ProductResponse createProduct(Product product, List<InnerVariantRequest> variants) {
        log.info("[createProduct] start create product ....");
        return upSertProduct(product, variants, null);
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public ProductResponse updateProduct(Product product, List<InnerVariantRequest> variants){
        log.info("[updateProduct] start update product ....");
        if(findRawProductById(product.getId()) == null){
            throw new ServiceException(EnumError.PRODUCT_ERR_NOT_FOUND_ID, "product.not.found.id",Map.of("id", product.getId()));

        }
        return upSertProduct(product, variants, product.getId());
    }
    
    private ProductResponse upSertProduct(Product product, List<InnerVariantRequest> variants, Long checkId){
        try {
            final String slug = SlugUtil.toSlug(product.getName());
            if (findRawProductBySlug(slug, checkId) != null) {
                throw new ServiceException(EnumError.PRODUCT_DATA_EXISTED_NAME, "product.exist.name",Map.of("name", product.getName()));
            }
            final Category category = Optional.ofNullable(product.getCategory())
            .map(c -> categoryService.findRawCategoryById(c.getId()))
            .orElse(null);

            if (category != null && !categoryService.isLeaf(category)) {
                throw new ServiceException(EnumError.CATEGORY_INVALID_ID, "product.category.invalid.id",Map.of("ID", category.getId()));
            }

            product.setSlug(slug);
            product.setCategory(category);

            // 3️⃣ Lưu Product chính
            Product createdProduct = productRepository.save(product);

            // 4️⃣ Nếu có variant mới xử lý tiếp
            if (variants == null || variants.isEmpty()) {
                return productMapper.toDto(createdProduct);
            }

            // 5️⃣ Lấy danh sách SKU ID và kiểm tra trùng
            final List<String> skuIds = variants.stream()
                    .map(v -> SlugUtil.toSlug(v.getSkuId()).toUpperCase())
                    .toList();

            final List<ProductSku> existingSkus = productSkuService.findListProductSku(skuIds);
            final Set<String> existingSkuSet = existingSkus.stream()
                    .map(ProductSku::getSku)
                    .map(String::toUpperCase)
                    .collect(Collectors.toSet());

            // 6️⃣ Lọc ra những SKU chưa tồn tại
            final List<ProductSku> newProductSkus = variants.stream()
                    .filter(v -> !existingSkuSet.contains(v.getSkuId().toUpperCase()))
                    .map(v -> ProductSku.builder()
                            .sku(v.getSkuId().toUpperCase())
                            .price(v.getPrice())
                            .stock(v.getStock())
                            .thumbnail(v.getThumbnail())
                            .product(createdProduct)
                            .build())
                    .toList();

            // 7️⃣ Lưu danh sách SKU mới
            final List<ProductSku> createdSkus = productSkuService.createListProductSku(newProductSkus);

            // 8️⃣ Gom tất cả SKU vừa tạo + đã có
            Map<String, ProductSku> allSkuMap = Stream.concat(createdSkus.stream(), existingSkus.stream())
                    .collect(Collectors.toMap(s -> s.getSku().toUpperCase(), Function.identity(),
                        (a, b) -> a // tránh duplicate key
                    ));

            //Lấy danh sách option value
            final List<String> optionValuesSlug = variants.stream()
                .flatMap(o -> o.getOptionValues().stream())
                .distinct() //set lọc phần tử trùng
                .collect(Collectors.toList());

            final Map<String, OptionValue> optionValueMap = 
                this.optionValueService.getRawListOptionValueBySlug(optionValuesSlug).stream()
                .collect(Collectors.toMap(OptionValue::getSlug, Function.identity()));

            // 9️⃣ Tạo danh sách Variant
            final List<Variant> variantEntities = variants.stream()
            .filter(v -> allSkuMap.containsKey(v.getSkuId().toUpperCase()))
            .flatMap(v -> v.getOptionValues().stream()
                //cú pháp method reference (tham chiếu hàm)
                //giống lambda => optionValueMap.get(optionValueSlug);
                .map(optionValueMap::get)
                //check null
                .filter(Objects::nonNull)
                .map(ov -> Variant.builder()
                        .product(createdProduct)
                        .sku(allSkuMap.get(v.getSkuId().toUpperCase()))
                        .option(ov.getOption())
                        .optionValue(ov)
                        .build())
            )
            // 3️⃣ collect thành list
            .collect(Collectors.toList());

            if (!variantEntities.isEmpty()) {
                variantService.createListVariant(variantEntities);
            }    

            return productMapper.toDto(createdProduct);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[upSertProduct] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationDTO getAllProduct(Pageable pageable, Specification spec){
        try {
            Page<Product> products = this.productRepository.findAll(spec, pageable);
            List<ProductResponse> listProducts = productMapper.toDto(products.getContent());
            return ConvertPagination.handleConvert(pageable, products, listProducts);
        } catch (Exception e) {
            log.error("[getAllProduct] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id){
        try {
            final Product product = findRawProductById(id);
            if(product == null){
                throw new ServiceException(EnumError.PRODUCT_ERR_NOT_FOUND_ID, "product.not.found.id",Map.of("id", id));

            }
            final List<Option> options = product.getVariants().stream()
                .map(o -> o.getOption()).distinct().collect(Collectors.toList()); 
            final List<OptionValue> optionValues = product.getVariants().stream()
                .map(o -> o.getOptionValue()).sorted(Comparator.comparing(o -> o.getId())).distinct().collect(Collectors.toList()); 
            
            final ProductResponse dto = productMapper.detailDto(product);
            dto.setOptions(optionMapper.toMiniDto(options));
            dto.setOptionValues(optionValueMapper.toMiniDto(optionValues));
            return dto;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[getProductById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
    @Transactional(readOnly = true)
    public Product findRawProductBySlug(String slug, Long checkId) {
        try {
            Optional<Product> productOptional = checkId != null ? this.productRepository.findBySlugAndIdNot(slug,checkId) :  this.productRepository.findBySlug(slug);
            return productOptional.isPresent() ? productOptional.get() : null;
        } catch (Exception e) {
            log.error("[findRawProductBySlug] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
    
    @Transactional(readOnly = true)
    public Product findRawProductById(Long id){
        try {
            Optional<Product> productOptional = this.productRepository.findById(id);
            return productOptional.isPresent() ? productOptional.get() : null;
        } catch (Exception e) {
            log.error("[findRawProductById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
}
