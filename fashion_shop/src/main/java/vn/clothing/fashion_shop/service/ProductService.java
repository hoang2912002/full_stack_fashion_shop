package vn.clothing.fashion_shop.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.clothing.fashion_shop.constants.util.SlugUtil;
import vn.clothing.fashion_shop.domain.Category;
import vn.clothing.fashion_shop.domain.OptionValue;
import vn.clothing.fashion_shop.domain.Product;
import vn.clothing.fashion_shop.domain.ProductSku;
import vn.clothing.fashion_shop.domain.Variant;
import vn.clothing.fashion_shop.mapper.ProductMapper;
import vn.clothing.fashion_shop.repository.ProductRepository;
import vn.clothing.fashion_shop.web.rest.DTO.product.GetProductDTO;
import vn.clothing.fashion_shop.web.rest.DTO.product.ValidationProductDTO.InnerVariant;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductSkuService productSkuService;
    private final VariantService variantService;
    private final ProductMapper productMapper;
    private final OptionValueService optionValueService;

    public ProductService(
            ProductRepository productRepository,
            CategoryService categoryService,
            ProductSkuService productSkuService,
            VariantService variantService,
            ProductMapper productMapper,
            OptionValueService optionValueService
        ) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.productSkuService = productSkuService;
        this.variantService = variantService;
        this.productMapper = productMapper;
        this.optionValueService = optionValueService;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public GetProductDTO createProduct(Product product, List<InnerVariant> variants) {
        
        // 1️⃣ Kiểm tra trùng slug
        String slug = SlugUtil.toSlug(product.getName());
        if (findRawProductBySlug(slug) != null) {
            throw new RuntimeException("Sản phẩm: " + product.getName() + " đã tồn tại");
        }

        // 2️⃣ Lấy Category nếu có
        Category category = null;
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            category = categoryService.findRawCategoryById(product.getCategory().getId());
            if(!categoryService.isLeaf(category)){
                throw new RuntimeException("Danh mục sản phẩm id: " + product.getCategory().getId() + " không hợp lệ");
            }
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
        List<String> skuIds = variants.stream()
                .map(v -> SlugUtil.toSlug(v.getSkuId()).toUpperCase())
                .toList();

        List<ProductSku> existingSkus = productSkuService.findListProductSku(skuIds);
        Set<String> existingSkuSet = existingSkus.stream()
                .map(ProductSku::getSku)
                .map(String::toUpperCase)
                .collect(Collectors.toSet());

        // 6️⃣ Lọc ra những SKU chưa tồn tại
        List<ProductSku> newProductSkus = variants.stream()
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
        List<ProductSku> createdSkus = productSkuService.createListProductSku(newProductSkus);

        // 8️⃣ Gom tất cả SKU vừa tạo + đã có
        Map<String, ProductSku> allSkuMap = Stream.concat(createdSkus.stream(), existingSkus.stream())
                .collect(Collectors.toMap(s -> s.getSku().toUpperCase(), Function.identity(),
                    (a, b) -> a // tránh duplicate key
                ));

        //Lấy danh sách option value
        List<String> optionValuesSlug = variants.stream()
            .flatMap(o -> o.getOptionValues().stream())
            .distinct() //set lọc phần tử trùng
            .collect(Collectors.toList());

        Map<String, OptionValue> optionValueMap = 
            this.optionValueService.getRawListOptionValueBySlug(optionValuesSlug).stream()
            .collect(Collectors.toMap(OptionValue::getSlug, Function.identity()));

        // 9️⃣ Tạo danh sách Variant
        List<Variant> variantEntities = variants.stream()
        .filter(v -> allSkuMap.containsKey(v.getSkuId().toUpperCase()))
        .flatMap(v -> v.getOptionValues().stream()
            //đây là cú pháp method reference (tham chiếu hàm)
            //có ý nghĩa giống lambda => optionValueMap.get(optionValueSlug);
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
    }

    public Product findRawProductBySlug(String slug) {
        Optional<Product> productOptional = this.productRepository.findBySlug(slug);
        return productOptional.isPresent() ? productOptional.get() : null;
    }
}
