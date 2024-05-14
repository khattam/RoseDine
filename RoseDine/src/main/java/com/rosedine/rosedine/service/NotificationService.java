package com.rosedine.rosedine.service;

import com.rosedine.rosedine.dto.NotificationFood;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<NotificationFood> getNotificationFoods(int userId) {
        String sql = "{CALL GetNotificationFoods(?)}";
        return jdbcTemplate.query(sql, new Object[]{userId}, new NotificationFoodRowMapper());
    }

    private static class NotificationFoodRowMapper implements RowMapper<NotificationFood> {
        @Override
        public NotificationFood mapRow(ResultSet rs, int rowNum) throws SQLException {
            NotificationFood food = new NotificationFood();
            food.setId(rs.getInt("ID"));
            food.setName(rs.getString("Name"));
            food.setMealType(rs.getString("MealType"));
            return food;
        }
    }
}