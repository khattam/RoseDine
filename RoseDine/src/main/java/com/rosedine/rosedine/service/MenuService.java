package com.rosedine.rosedine.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class MenuService { //also another way to do using @procedure annotation, take a look, this just keeps it cleaner, but less verbose

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MenuService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int addMenu(Date date, String timings, String type, int restId) {
        String sql = "{ CALL InsertMenu(?, ?, ?, ?) }";
        return jdbcTemplate.queryForObject(sql, new Object[]{date, timings, type, restId}, Integer.class);
    }

    public int addMenuItem(String name, int macroId, int dietaryRestrictionsId, int overallStars) {
        String sql = "{ CALL InsertMenuItem(?, ?, ?, ?) }";
        return jdbcTemplate.queryForObject(sql, new Object[]{name, macroId, dietaryRestrictionsId, overallStars}, Integer.class);
    }

    public boolean addIncludesItem(int menuId, int menuItemId) {
        String sql = "{ CALL InsertIncludesItem(?, ?) }";
        return jdbcTemplate.update(sql, menuId, menuItemId) > 0;
    }
}