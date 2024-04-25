package com.rosedine.rosedine.service;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.*;

@Service
public class JsonDataService {
    private static final String JSON_FILE_PATH = "PyScraping/json_output.json";
    private static final Map<String, String> mealTimings = Map.of(
            "Breakfast", "7 to 10",
            "Brunch", "10 to 2",
            "Lunch", "11 to 2",
            "Dinner", "5 to 8"
    );

    private final MenuService menuService;

    public JsonDataService(@Autowired MenuService menuService) {
        this.menuService = menuService;
    }

    public void parseAndSaveJsonData() {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(JSON_FILE_PATH));

            jsonObject.forEach((dateStr, value) -> {
                Date date = Date.valueOf((String) dateStr);
                JSONObject mealtypesObject = (JSONObject) value;

                mealtypesObject.forEach((mealType, menuItems) -> {
                    String timings = mealTimings.get(mealType);
                    int restaurantId = 1;

                    int menuID = menuService.addMenu(date, timings, (String) mealType, restaurantId);
                    if (menuID != -1) {
                        JSONArray itemsArray = (JSONArray) menuItems;
                        for (Object itemName : itemsArray) {
                            String name = (String) itemName;
                            int macroId = 1;
                            int dietaryId = 1;
                            int overallStars = 5;

                            int menuItemID = menuService.addMenuItem(name, macroId, dietaryId, overallStars);
                            if (menuItemID != -1) {
                                menuService.addIncludesItem(menuID, menuItemID);
                            }
                        }
                    }
                });
            });

            System.out.println("All menus and menu items inserted successfully.");

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
