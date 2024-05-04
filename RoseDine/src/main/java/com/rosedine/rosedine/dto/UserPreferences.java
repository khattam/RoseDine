package com.rosedine.rosedine.dto;

public class UserPreferences {
    private boolean isVegan;
    private boolean isVegetarian;
    private boolean isGlutenFree;
    private int Protein;
    private int Carbohydrates;
    private int Fats;
    private int Calories;

    // Constructors
    public UserPreferences() {}

    public UserPreferences(boolean isVegan, boolean isVegetarian, boolean isGlutenFree, int targetProtein, int targetCarbs, int targetFats, int targetCalories) {
        this.isVegan = isVegan;
        this.isVegetarian = isVegetarian;
        this.isGlutenFree = isGlutenFree;
        this.Protein = targetProtein;
        this.Carbohydrates = targetCarbs;
        this.Fats = targetFats;
        this.Calories = targetCalories;
    }

    public boolean isVegan() {
        return isVegan;
    }

    public void setVegan(boolean vegan) {
        isVegan = vegan;
    }

    public boolean isVegetarian() {
        return isVegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        isVegetarian = vegetarian;
    }

    public boolean isGlutenFree() {
        return isGlutenFree;
    }

    public void setGlutenFree(boolean isGlutenFree) {
        this.isGlutenFree = isGlutenFree;
    }

    public void setProtein(int protein) {
        this.Protein = protein;
    }

    public void setCarbohydrates(int carbohydrates) {
        this.Carbohydrates = carbohydrates;
    }

    public void setFats(int fats) {
        this.Fats = fats;
    }

    public void setCalories(int calories) {
        this.Calories = calories;
    }

    public int getProtein() {
        return Protein;
    }

    public int getCarbohydrates() {
        return Carbohydrates;
    }

    public int getFats() {
        return Fats;
    }

    public int getCalories() {
        return Calories;
    }
}
