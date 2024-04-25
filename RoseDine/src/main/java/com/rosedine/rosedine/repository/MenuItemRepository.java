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
            MenuItemDTO menuItem = new MenuItemDTO();
            menuItem.setName(rs.getString("Name"));
            menuItem.setOverallStars(rs.getInt("OverallStars"));
            menuItem.setFats(rs.getDouble("Fats"));
            menuItem.setProtein(rs.getDouble("Protein"));
            menuItem.setNetCarbs(rs.getDouble("Net_Carbs"));
            menuItem.setCalories(rs.getDouble("Calories"));
            menuItem.setTotalCarbs(rs.getDouble("Total_Carbs"));
            menuItem.setVegan(rs.getBoolean("Is_Vegan"));
            menuItem.setVegetarian(rs.getBoolean("Is_Vegetarian"));
            menuItem.setGlutenFree(rs.getBoolean("Is_Gluten_Free"));
            return menuItem;
        }
    }
}