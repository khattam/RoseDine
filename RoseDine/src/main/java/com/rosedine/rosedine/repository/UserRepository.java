package com.rosedine.rosedine.repository;

import com.rosedine.rosedine.dto.UserPreferences;
import com.rosedine.rosedine.repository.FileLogger;

import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.sql.ResultSet;
import java.util.Map;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // In UserRepository.java
    // In UserRepository.java
    public UserPreferences getUserPreferences(int userId, String mealType) {
        UserPreferences userPreferences = new UserPreferences();
        try {
            // Query Dietary Restrictions
            String dietarySql = "SELECT Is_Vegan, Is_Vegetarian, Is_Gluten_Free FROM DietaryRestrictions "
                    + "WHERE ID = (SELECT DietaryRestrictions_ID FROM [User] WHERE UserID = ?)";
            Map<String, Object> dietaryResult = jdbcTemplate.queryForMap(dietarySql, userId);
            userPreferences.setVegan((boolean) dietaryResult.getOrDefault("Is_Vegan", false));
            userPreferences.setVegetarian((boolean) dietaryResult.getOrDefault("Is_Vegetarian", false));
            userPreferences.setGlutenFree((boolean) dietaryResult.getOrDefault("Is_Gluten_Free", false));

            // Query Macros
            String macroSql = "SELECT Fats, Protein, Carbs, Calories FROM Macros "
                    + "WHERE ID = (SELECT Macro_ID FROM MacroGoals WHERE User_ID = ? AND Type = ?)";
            Map<String, Object> macroResult = jdbcTemplate.queryForMap(macroSql, userId, mealType);
            userPreferences.setFats((int) macroResult.getOrDefault("Fats", 0));
            userPreferences.setProtein((int) macroResult.getOrDefault("Protein", 0));
            userPreferences.setCarbohydrates((int) macroResult.getOrDefault("Carbs", 0));
            userPreferences.setCalories((int) macroResult.getOrDefault("Calories", 0));

        } catch (Exception e) {
            System.err.println("Error fetching user preferences: " + e.getMessage());
            // Consider returning an empty UserPreferences or handle the exception as required
        }
        return userPreferences;
    }




}
