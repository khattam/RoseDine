package com.rosedine.rosedine.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rosedine.rosedine.dto.MenuItemDTO;
import com.rosedine.rosedine.dto.UserPreferences;
import com.rosedine.rosedine.service.MenuItemService;
import com.rosedine.rosedine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class RecommendationService {
    private final MenuItemService menuItemService;
    private final UserService userService;

    @Autowired
    public RecommendationService(MenuItemService menuItemService, UserService userService) {
        this.menuItemService = menuItemService;
        this.userService = userService;
    }

    public List<Map<String, Object>> getRecommendations(int userId, String mealType) {
        UserPreferences userPreferences = userService.getUserPreferences(userId, mealType);
        List<MenuItemDTO> menuItems = menuItemService.getMenuItemsByDateAndType(LocalDate.now(), mealType);

        List<MenuItemDTO> filteredItems = menuItems.stream()
                .filter(item -> matchesPreferences(item, userPreferences))
                .sorted(Comparator.comparingInt(item -> calculateProteinPerCalorie((MenuItemDTO) item)).reversed())
                .collect(Collectors.toList());

        List<MenuItemDTO> recommendations = selectRecommendations(filteredItems, userPreferences);

        int totalProtein = recommendations.stream().mapToInt(MenuItemDTO::getProtein).sum();
        int totalCarbs = recommendations.stream().mapToInt(MenuItemDTO::getCarbs).sum();
        int totalFats = recommendations.stream().mapToInt(MenuItemDTO::getFat).sum();
        int totalCalories = recommendations.stream().mapToInt(MenuItemDTO::getCalories).sum();

        return recommendations.stream()
                .map(item -> {
                    Map<String, Object> recommendation = new HashMap<>();
                    recommendation.put("item", item);
                    recommendation.put("proteinMatch", calculateMacroMatch(item.getProtein(), userPreferences.getProtein()));
                    recommendation.put("carbsMatch", calculateMacroMatch(item.getCarbs(), userPreferences.getCarbs()));
                    recommendation.put("fatsMatch", calculateMacroMatch(item.getFat(), userPreferences.getFats()));
                    recommendation.put("caloriesMatch", calculateMacroMatch(item.getCalories(), userPreferences.getCalories()));
                    recommendation.put("totalProtein", totalProtein);
                    recommendation.put("totalCarbs", totalCarbs);
                    recommendation.put("totalFats", totalFats);
                    recommendation.put("totalCalories", totalCalories);
                    return recommendation;
                })
                .collect(Collectors.toList());
    }

    private List<MenuItemDTO> selectRecommendations(List<MenuItemDTO> items, UserPreferences userPreferences) {
        List<MenuItemDTO> result = new ArrayList<>();
        int remainingCalories = userPreferences.getCalories();

        for (MenuItemDTO item : items) {
            if (item.getCalories() <= remainingCalories) {
                result.add(item);
                remainingCalories -= item.getCalories();
            }
            if (remainingCalories <= 0) break; // Stop adding if calorie limit is reached
        }

        return result;
    }

    private boolean matchesPreferences(MenuItemDTO item, UserPreferences userPreferences) {
        return (item.isVegan() || !userPreferences.isVegan())
                && (item.isVegetarian() || !userPreferences.isVegetarian())
                && (item.isGlutenFree() || !userPreferences.isGlutenFree());
    }

    private int calculateProteinPerCalorie(MenuItemDTO item) {
        return (item.getCalories() == 0) ? 0 : item.getProtein() * 100 / item.getCalories();
    }

    private double calculateMacroMatch(int actual, int target) {
        return (target == 0) ? 0 : (actual / (double) target) * 100;
    }
}
