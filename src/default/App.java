// App.java (Located in the 'default' package)

import db.services.DatabaseConnectionService;
import java.sql.Connection;
import db.services.MenuService;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.io.IOException;



public class App {
    public static void main(String[] args) {

        //Make sure you change the filepath here
        DatabaseConnectionService dbService = new DatabaseConnectionService("C:\\Users\\khattam\\IdeaProjects\\course-project-s3g3-rosediner\\src\\default\\dbms.properties.txt");

        // Attempt to connect to the database
        if (dbService.connect()) {
            System.out.println("Connected to the database successfully.");

            // Instantiate MenuService with the connection
            MenuService menuService = new MenuService(dbService);

            //Chane filepath here
            Path pathToOutput = Paths.get("C:\\Users\\khattam\\IdeaProjects\\course-project-s3g3-rosediner\\src\\PyScraping\\output.txt");

            try {
                // Read all lines from the output.txt file
                List<String> allLines = Files.readAllLines(pathToOutput);
                LocalDate date = null;
                String mealType = null;

                // Iterate through the lines
                for (String line : allLines) {
                    if (line.startsWith("Scraping")) {
                        // Extract date and meal type, e.g., "Scraping Breakfast for 2024-03-31"
                        String[] parts = line.split(" ");
                        mealType = parts[1];
                        date = LocalDate.parse(parts[3], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        menuService.addMenu(date, mealType, "default timing value");

                    } else if (line.trim().startsWith("Item:")) {
                        // Extract item name, e.g., "Item: French Fries"
                        String itemName = line.trim().substring(6);
                        menuService.addMenuItem(itemName);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            // Remember to close the connection when you're done
            dbService.closeConnection();
        } else {
            System.out.println("Failed to connect to the database.");
        }
    }
}
