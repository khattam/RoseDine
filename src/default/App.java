// App.java (Located in the 'default' package)

import db.services.DatabaseConnectionService;
import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        // Assuming 'dbms.properties.txt' is in the root of your project folder.
        // If it's located elsewhere, provide the correct path relative to the project root.
        DatabaseConnectionService dbService = new DatabaseConnectionService("C:\\Users\\srivasa\\course-project-s3g3-rosediner\\src\\default\\dbms.properties.txt");

        // Attempt to connect to the database
        if (dbService.connect()) {
            System.out.println("Connected to the database successfully.");

            // Get the connection object to use it for executing SQL statements
            Connection conn = dbService.getConnection();

            // TODO: Use 'conn' to perform database operations

            // Remember to close the connection when you're done
            dbService.closeConnection();
        } else {
            System.out.println("Failed to connect to the database.");
        }
    }
}
