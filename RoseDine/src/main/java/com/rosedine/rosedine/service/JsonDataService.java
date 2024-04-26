package com.rosedine.rosedine.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.Map;

@Service
public class JsonDataService {
    private static final String JSON_FILE_PATH = "PyScraping/nutrition_info.json";
    private static final Map<String, String> mealTimings = Map.of(
            "Breakfast", "7 to 10",
            "Brunch", "10 to 2",
            "Lunch", "11 to 2",
            "Dinner", "5 to 8"
    );

    private final MenuService menuService;

    @Autowired
    public JsonDataService(MenuService menuService) {
        this.menuService = menuService;
    }

    public void parseAndSaveJsonData() {
        JSONParser parser = new JSONParser();
        try {
            JSONArray jsonFileArray = (JSONArray) parser.parse(new FileReader(JSON_FILE_PATH));

            for (Object item : jsonFileArray) {
                JSONObject menuItemObject = (JSONObject) item;

                Date date = Date.valueOf((String) menuItemObject.get("Date"));
                String mealType = (String) menuItemObject.get("MealType");
                String itemName = (String) menuItemObject.get("ItemName");
                JSONObject nutrition = (JSONObject) menuItemObject.get("Nutrition");

                int protein = Math.round(Float.parseFloat(((String) nutrition.get("protein")).replaceAll("g", "").trim()));
                int fats = Math.round(Float.parseFloat(((String) nutrition.get("fat")).replaceAll("g", "").trim()));
                int carbs = Math.round(Float.parseFloat(((String) nutrition.get("carbs")).replaceAll("g", "").trim()));
                int calories = Math.round(Float.parseFloat((String) nutrition.get("calories")));

                boolean isVegan = (Boolean) nutrition.get("isVegan");
                boolean isVegetarian = (Boolean) nutrition.get("isVegetarian");
                boolean isGlutenFree = (Boolean) nutrition.get("isGlutenFree");

                String timings = mealTimings.get(mealType);
                int restaurantId = 1;

                try {
                    int menuID = menuService.addMenu(date, timings, mealType, restaurantId);
                    int menuItemID = menuService.getMenuItemByName(itemName);

                    if (menuItemID == -1) {
                        int macroId = menuService.addMacro(fats, protein, carbs, calories);
                        int dietaryId = menuService.addDietaryRestriction(isVegan, isVegetarian, isGlutenFree);
                        int overallStars = 5;
                        menuItemID = menuService.addMenuItem(itemName, macroId, dietaryId, overallStars);
                    }

                    menuService.addIncludesItem(menuID, menuItemID);
                } catch (Exception e) {
                    System.out.println("Error inserting menu item: " + itemName);
                    e.printStackTrace();
                    throw e;
                }
            }
            System.out.println("All menus and menu items inserted successfully.");
        } catch (IOException | ParseException e) {
            System.out.println("Failed to parse or process the JSON file.");
            e.printStackTrace();
            throw new RuntimeException("Failed to parse or process the JSON file.", e);
        }
    }
}
