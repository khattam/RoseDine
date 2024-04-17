public class App {
    public static void main(String[] args) {
        DatabaseConnectionService dbService = new DatabaseConnectionService("your_server_name", "your_database_name");
        
        // Attempt to connect to the database with the provided username and password
        if (dbService.connect("your_username", "your_password")) {
            // Connected successfully
            // You can now get the connection object and use it for executing SQL statements
            Connection conn = dbService.getConnection();
            
            // TODO: Use `conn` to perform database operations
            
            // After you are done with the database operations, close the connection
            dbService.closeConnection();
        } else {
            // Connection failed
            System.out.println("Failed to connect to the database.");
        }
    }
}
