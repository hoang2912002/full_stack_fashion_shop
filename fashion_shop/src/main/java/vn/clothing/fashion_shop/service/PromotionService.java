package vn.clothing.fashion_shop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.clothing.fashion_shop.domain.Promotion;
import vn.clothing.fashion_shop.repository.PromotionRepository;
import vn.clothing.fashion_shop.web.rest.DTO.requests.CategoryRequest.InnerCategoryRequest;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ProductRequest.InnerProductRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PromotionResponse;

public interface PromotionService {
    PromotionResponse createPromotion(Promotion promotion,List<InnerCategoryRequest> categoryRequests, List<InnerProductRequest> productRequests);
    PromotionResponse updatePromotion(Promotion promotion, List<InnerCategoryRequest> categoryRequests, List<InnerProductRequest> productRequests);
    Promotion getPromotionById(Long id);
    PaginationResponse getAllPromotion(Long id);
}
