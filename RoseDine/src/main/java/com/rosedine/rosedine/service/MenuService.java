package com.rosedine.rosedine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Types;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MenuService {

    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MenuService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Method to add a menu and return the ID of the newly added or existing menu
    @Transactional
    public int addMenu(Date date, String timings, String type, int restId) {
        logger.debug("Attempting to add menu with Date: {}, Timings: {}, Type: {}, Restaurant ID: {}", date, timings, type, restId);
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("InsertMenu")
                .declareParameters(
                        new SqlParameter("Date", Types.DATE),
                        new SqlParameter("Timings", Types.NVARCHAR),
                        new SqlParameter("Type", Types.NVARCHAR),
                        new SqlParameter("Rest_ID", Types.INTEGER),
                        new SqlOutParameter("ID", Types.INTEGER)
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("Date", date);
        inParams.put("Timings", timings);
        inParams.put("Type", type);
        inParams.put("Rest_ID", restId);

        Map<String, Object> out = jdbcCall.execute(inParams);
        int menuId = (Integer) out.get("ID");
        logger.debug("Menu successfully added with ID: {}", menuId);
        return menuId;
    }

    @Transactional
    public int addMenuItem(String name, int macroId, int dietaryRestrictionsId, int overallStars) {
        logger.debug("Attempting to add menu item with Name: {}, Macro ID: {}, Dietary Restrictions ID: {}, Overall Stars: {}", name, macroId, dietaryRestrictionsId, overallStars);
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("InsertMenuItem")
                .declareParameters(
                        new SqlParameter("Name", Types.NVARCHAR),
                        new SqlParameter("Macro_ID", Types.INTEGER),
                        new SqlParameter("DietaryRestrictions_ID", Types.INTEGER),
                        new SqlParameter("OverallStars", Types.INTEGER),
                        new SqlOutParameter("ID", Types.INTEGER)
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("Name", name);
        inParams.put("Macro_ID", macroId);
        inParams.put("DietaryRestrictions_ID", dietaryRestrictionsId);
        inParams.put("OverallStars", overallStars);

        Map<String, Object> out = jdbcCall.execute(inParams);
        int menuItemId = (Integer) out.get("ID");
        logger.debug("Menu item successfully added with ID: {}", menuItemId);
        return menuItemId;
    }

    @Transactional
    public int addMacro(int fats, int protein, int carbs, int calories) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("InsertMacro")
                .declareParameters(
                        new SqlParameter("Fats", Types.INTEGER),
                        new SqlParameter("Protein", Types.INTEGER),
                        new SqlParameter("Carbs", Types.INTEGER),
                        new SqlParameter("Calories", Types.INTEGER),
                        new SqlOutParameter("ID", Types.INTEGER)
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("Fats", fats);
        inParams.put("Protein", protein);
        inParams.put("Carbs", carbs);
        inParams.put("Calories", calories);

        Map<String, Object> out = jdbcCall.execute(inParams);
        return (Integer) out.get("ID");
    }

    @Transactional
    public int addDietaryRestriction(boolean isVegan, boolean isVegetarian, boolean isGlutenFree) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("InsertDietaryRestrictions")
                .declareParameters(
                        new SqlParameter("IsVegan", Types.BIT),
                        new SqlParameter("IsVegetarian", Types.BIT),
                        new SqlParameter("IsGlutenFree", Types.BIT),
                        new SqlOutParameter("ID", Types.INTEGER)
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("IsVegan", isVegan);
        inParams.put("IsVegetarian", isVegetarian);
        inParams.put("IsGlutenFree", isGlutenFree);

        Map<String, Object> out = jdbcCall.execute(inParams);
        return (Integer) out.get("ID");
    }


    public void addIncludesItem(int menuId, int menuItemId) {
        logger.debug("Linking menu ID: {} with menu item ID: {}", menuId, menuItemId);
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("InsertIncludesItem")
                .declareParameters(
                        new SqlParameter("Menu_ID", Types.INTEGER),
                        new SqlParameter("MenuItem_ID", Types.INTEGER)
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("Menu_ID", menuId);
        inParams.put("MenuItem_ID", menuItemId);

        jdbcCall.execute(inParams);
        logger.debug("Successfully linked menu ID: {} with menu item ID: {}", menuId, menuItemId);
    }

    public int getMenuItemByName(String name) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GetMenuItemByName")
                .declareParameters(
                        new SqlParameter("Name", Types.NVARCHAR),
                        new SqlOutParameter("MenuItemID", Types.INTEGER)
                );

        SqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("Name", name);

        Map<String, Object> out = jdbcCall.execute(inParams);
        return (Integer) out.get("MenuItemID");  // Retrieve output value
    }


}