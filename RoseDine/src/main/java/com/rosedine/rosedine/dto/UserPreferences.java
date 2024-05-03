package com.rosedine.rosedine.dto;

public class UserPreferences {
    private boolean isVegan;
    private boolean isVegetarian;
    private boolean isGlutenFree;
    private int targetProtein;
    private int targetCarbs;
    private int targetFats;
    private int targetCalories;

    // Constructors
    public UserPreferences() {}

    public UserPreferences(boolean isVegan, boolean isVegetarian, boolean isGlutenFree, int targetProtein, int targetCarbs, int targetFats, int targetCalories) {
        this.isVegan = isVegan;
        this.isVegetarian = isVegetarian;
        this.isGlutenFree = isGlutenFree;
        this.targetProtein = targetProtein;
        this.targetCarbs = targetCarbs;
        this.targetFats = targetFats;
        this.targetCalories = targetCalories;
    }



    // Getters and setters
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

    public void setGlutenFree(boolean glutenFree) {
        isGlutenFree = glutenFree;
    }


}
