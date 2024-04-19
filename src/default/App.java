import db.services.DatabaseConnectionService;
import db.services.MenuService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.Map;

public class App {
    private static final String JSON_FILE_PATH = "src/PyScraping/json_output.json";
    private static final Map<String, String> mealTimings = Map.of(
            "Breakfast", "7 to 10",
            "Brunch", "10 to 2",
            "Lunch", "11 to 2",
            "Dinner", "5 to 8"
    );

    public static void main(String[] args) {
        DatabaseConnectionService dbService = new DatabaseConnectionService("src/default/dbms.properties.txt");

        if (dbService.connect()) {
            try {
                System.out.println("Connected to the database successfully.");
                MenuService menuService = new MenuService(dbService);

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
            } finally {
                dbService.closeConnection();
            }
        } else {
            System.out.println("Failed to connect to the database.");
        }
    }
}