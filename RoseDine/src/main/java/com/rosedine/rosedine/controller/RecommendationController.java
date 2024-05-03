package com.rosedine.rosedine.controller;

import com.rosedine.rosedine.dto.MenuItemDTO;
import com.rosedine.rosedine.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping
    public List<MenuItemDTO> getRecommendations(@RequestParam int userId, @RequestParam String mealType) {
        // Pass the mealType parameter when calling getRecommendations
        return recommendationService.getRecommendations(userId, mealType);
    }
}
