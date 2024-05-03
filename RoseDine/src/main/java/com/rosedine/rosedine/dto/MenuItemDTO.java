package com.rosedine.rosedine.dto;

import lombok.Data;

@Data
public class MenuItemDTO {
    private String name;
    private int overallStars;
    private int fats;
    private int protein;
    private int carbs;
    private int calories;
    private boolean isVegan;
    private boolean isVegetarian;
    private boolean isGlutenFree;

    // Constructor with all arguments
    public MenuItemDTO(String name, int overallStars, int fats, int protein, int carbs, int calories, boolean isVegan, boolean isVegetarian, boolean isGlutenFree) {
        this.name = name;
        this.overallStars = overallStars;
        this.fats = fats;
        this.protein = protein;
        this.carbs = carbs;
        this.calories = calories;
        this.isVegan = isVegan;
        this.isVegetarian = isVegetarian;
        this.isGlutenFree = isGlutenFree;
    }
}
