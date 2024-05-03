package com.rosedine.rosedine.service;

import com.rosedine.rosedine.dto.MenuItemDTO;
import com.rosedine.rosedine.dto.UserPreferences;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<MenuItemDTO> getRecommendations(int userId, String mealType) {
        // Fetch user preferences using both userId and mealType
        UserPreferences userPreferences = userService.getUserPreferences(userId, mealType);

        // Fetch all available menu items
        List<MenuItemDTO> menuItems = menuItemService.getAllMenuItems();

        // Apply recommendation logic here
        return menuItems.stream()
                .filter(item -> matchesPreferences(item, userPreferences))
                .sorted((item1, item2) -> compareByNutritionalValue(item1, item2, userPreferences))
                .collect(Collectors.toList());
    }

    private boolean matchesPreferences(MenuItemDTO item, UserPreferences userPreferences) {
        // Check if the item matches user dietary preferences
        return (item.isVegan() || !userPreferences.isVegan())
                && (item.isVegetarian() || !userPreferences.isVegetarian())
                && (item.isGlutenFree() || !userPreferences.isGlutenFree());
    }

    private int compareByNutritionalValue(MenuItemDTO item1, MenuItemDTO item2, UserPreferences userPreferences) {
        // Implement comparison logic based on the user's nutritional goals
        return Integer.compare(item1.getProtein(), item2.getProtein());
    }
}
