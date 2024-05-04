package com.rosedine.rosedine.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.dao.EmptyResultDataAccessException;

@Service
public class ReviewService {
    private final JdbcTemplate jdbcTemplate;

    public ReviewService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void sendReview(int userId, int menuItemId, int stars) {
        String sql = "EXEC InsertOrUpdateReview ?, ?, ?";
        jdbcTemplate.update(sql, userId, menuItemId, stars);
    }

    public Integer getUserRatingForMenuItem(int userId, int menuItemId) {
        String sql = "EXEC GetUserRatingForMenuItem ?, ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userId, menuItemId}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }
}
