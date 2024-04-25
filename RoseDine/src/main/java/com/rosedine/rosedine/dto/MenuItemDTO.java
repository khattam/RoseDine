package com.rosedine.rosedine.dto;

import lombok.Data;

@Data
public class MenuItemDTO {
    private String name;
    private int overallStars;
    private double fats;
    private double protein;
    private double netCarbs;
    private double calories;
    private double totalCarbs;
    private boolean isVegan;
    private boolean isVegetarian;
    private boolean isGlutenFree;
}
