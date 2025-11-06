package vn.clothing.fashion_shop.service;

import org.springframework.stereotype.Service;

import vn.clothing.fashion_shop.domain.Promotion;
import vn.clothing.fashion_shop.repository.PromotionRepository;

@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public Promotion createPromotion(Promotion promotion){
        return null;
    }

    
}
