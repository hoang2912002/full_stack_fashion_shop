package vn.clothing.fashion_shop.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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

    public ProductService(
            ProductRepository productRepository,
            CategoryService categoryService,
            ProductSkuService productSkuService,
            VariantService variantService,
            ProductMapper productMapper
        ) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.productSkuService = productSkuService;
        this.variantService = variantService;
        this.productMapper = productMapper;
    }

    @Transactional
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
        }
        product.setSlug(slug);
        product.setCategory(category);

        // 3️⃣ Lưu Product chính
        Product createdProduct = productRepository.saveAndFlush(product);

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

        // 8️⃣ Gom tất cả SKU vừa tạo + đã có (nếu cần link)
        Map<String, ProductSku> allSkuMap = Stream.concat(createdSkus.stream(), existingSkus.stream())
                .collect(Collectors.toMap(s -> s.getSku().toUpperCase(), s -> s));

        // 9️⃣ Tạo danh sách Variant
        List<Variant> variantEntities = new ArrayList<>();
        for (InnerVariant variant : variants) {
            ProductSku sku = allSkuMap.get(variant.getSkuId().toUpperCase());
            if (sku == null)
                continue;

            for (OptionValue ov : variant.getOptionValues()) {
                Variant variantEntity = Variant.builder()
                        .product(createdProduct)
                        .sku(sku)
                        .option(ov.getOption())
                        .optionValue(ov)
                        .build();

                // ⚠️ Kiểm tra variant đã tồn tại chưa
                Variant exists = variantService.findVariantFromProduct(variantEntity);
                if (exists == null)continue;

                variantEntities.add(variantEntity);
            }
        }

        // 🔟 Lưu các variant mới
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
