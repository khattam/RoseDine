package com.rosedine.rosedine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserPreferencesService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void updateDietaryRestriction(int userId, String restrictionName, boolean restrictionValue) {
        String sql = "EXEC UpdateDietaryRestriction @UserId = ?, @RestrictionName = ?, @RestrictionValue = ?";
        jdbcTemplate.update(sql, userId, restrictionName, restrictionValue);
    }

    public void updateMacro(int userId, String mealType, String macroName, int macroValue) {
        String sql = "EXEC UpdateUserMacro @UserId = ?, @MealType = ?, @MacroName = ?, @MacroValue = ?";
        jdbcTemplate.update(sql, userId, mealType, macroName, macroValue);
    }

    public Map<String, Object> getUserPreferences(int userId) {
        String sql = "EXEC GetUserPreferences @UserId = ?";
        return jdbcTemplate.queryForMap(sql, userId);
    }

}