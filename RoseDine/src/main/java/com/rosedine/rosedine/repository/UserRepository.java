package com.rosedine.rosedine.repository;

import com.rosedine.rosedine.dto.UserPreferences;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserPreferences getUserPreferences(int userId, String mealType) {
        // Query for dietary restrictions
        String dietarySql = "SELECT Is_Vegan, Is_Vegetarian, Is_Gluten_Free FROM DietaryRestrictions "
                + "WHERE ID = (SELECT DietaryRestrictions_ID FROM Users WHERE UserID = ?)";
        Map<String, Object> dietaryResult = jdbcTemplate.queryForMap(dietarySql, userId);

        // Ensure the Macro_ID is retrieved correctly
        String macroIdSql = "SELECT Macro_ID FROM MacroGoals WHERE User_ID = ? AND Type = ?";
        Integer macroId = jdbcTemplate.queryForObject(macroIdSql, new Object[]{userId, mealType}, Integer.class);

        // Check if Macro_ID is valid
        if (macroId == null || macroId <= 0) {
            System.err.println("Invalid Macro ID for user: " + userId + " and mealType: " + mealType);
            return new UserPreferences(false, false, false, 0, 0, 0, 0); // Return empty preferences
        }

        // Retrieve the macros
        String macroSql = "SELECT Fats, Protein, Carbs, Calories FROM Macros WHERE ID = ?";
        Map<String, Object> macroResult = jdbcTemplate.queryForMap(macroSql, macroId);

        // Ensure macros are returned correctly
        Integer fats = macroResult.get("Fats") != null ? (Integer) macroResult.get("Fats") : 0;
        Integer protein = macroResult.get("Protein") != null ? (Integer) macroResult.get("Protein") : 0;
        Integer carbs = macroResult.get("Carbs") != null ? (Integer) macroResult.get("Carbs") : 0;
        Integer calories = macroResult.get("Calories") != null ? (Integer) macroResult.get("Calories") : 0;

        return new UserPreferences(
                (boolean) dietaryResult.getOrDefault("Is_Vegan", false),
                (boolean) dietaryResult.getOrDefault("Is_Vegetarian", false),
                (boolean) dietaryResult.getOrDefault("Is_Gluten_Free", false),
                protein,
                carbs,
                fats,
                calories
        );
    }
}
