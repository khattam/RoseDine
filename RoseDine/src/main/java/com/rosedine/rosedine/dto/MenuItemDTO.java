package com.rosedine.rosedine.dto;

import lombok.Data;

@Data
public class MenuItemDTO {
    private String name;
    private int overallStars;
    private int fats;
    private int protein;
    private int Carbs;
    private int calories;
    private boolean isVegan;
    private boolean isVegetarian;
    private boolean isGlutenFree;
}
