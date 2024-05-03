package com.rosedine.rosedine.repository;

import com.rosedine.rosedine.dto.MenuItemDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class MenuItemRepository {
    private final JdbcTemplate jdbcTemplate;

    public MenuItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MenuItemDTO> getMenuItemsByDateAndType(LocalDate date, String type) {
        String sql = "EXEC GetMenuItemsByDateAndType ?, ?";
        return jdbcTemplate.query(sql, new Object[]{date, type}, new MenuItemRowMapper());
    }


    private static class MenuItemRowMapper implements RowMapper<MenuItemDTO> {
        @Override
        public MenuItemDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            String name = rs.getString("Name");
            int overallStars = rs.getInt("OverallStars");
            int fats = rs.getInt("Fats");
            int protein = rs.getInt("Protein");
            int carbs = rs.getInt("Carbs");
            int calories = rs.getInt("Calories");
            boolean isVegan = rs.getBoolean("Is_Vegan");
            boolean isVegetarian = rs.getBoolean("Is_Vegetarian");
            boolean isGlutenFree = rs.getBoolean("Is_Gluten_Free");

            return new MenuItemDTO(name, overallStars, fats, protein, carbs, calories, isVegan, isVegetarian, isGlutenFree);
        }
    }

    public List<MenuItemDTO> getAllMenuItems() {
        String sql = "SELECT * FROM MenuItem";
        return jdbcTemplate.query(sql, new MenuItemRowMapper());
    }



}