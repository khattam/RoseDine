package db.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DatabaseConnectionService {

    private Connection connection = null;
    private String dbmsPropertiesPath;

    public DatabaseConnectionService(String dbmsPropertiesPath) {
        this.dbmsPropertiesPath = dbmsPropertiesPath;
    }

    public boolean connect() {
        Properties dbProperties = new Properties();
        try (FileInputStream fis = new FileInputStream(this.dbmsPropertiesPath)) {
            dbProperties.load(fis);
            String serverName = dbProperties.getProperty("serverName");
            String databaseName = dbProperties.getProperty("databaseName");
            String user = dbProperties.getProperty("serverUsername");
            String pass = dbProperties.getProperty("serverPassword");
            System.out.println("Server name: " + dbProperties.getProperty("serverName"));
            System.out.println("Database name: " + dbProperties.getProperty("databaseName"));



            String connectionString = "jdbc:sqlserver://" + serverName + ";databaseName=" + databaseName +
                    ";user=" + user + ";password=" + pass + ";encrypt=true;trustServerCertificate=true;";

            if (this.connection == null || this.connection.isClosed()) {
                this.connection = DriverManager.getConnection(connectionString);
                System.out.println("Connected to database successfully.");
                return true;
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to the database.");
            return false;
        }
        return false;
    }

    public Connection getConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
               // System.out.println("Returning an open connection.");
                return this.connection;
            } else {
                //System.out.println("Connection is closed. Attempting to reconnect...");
                return reconnect();
            }
        } catch (SQLException e) {
            //System.err.println("Error while checking connection status: " + e.getMessage());
            return null;
        }
    }

    private Connection reconnect() {
        try {
            this.connection = DriverManager.getConnection(getConnectionString());
            //System.out.println("Reconnected to the database successfully.");
            return this.connection;
        } catch (SQLException | IOException e) {
            //System.err.println("Failed to reconnect to the database: " + e.getMessage());
            return null;
        }
    }

    private String getConnectionString() throws IOException {
        Properties dbProperties = new Properties();
        try (FileInputStream fis = new FileInputStream(this.dbmsPropertiesPath)) {
            dbProperties.load(fis);
        }

        String serverName = dbProperties.getProperty("serverName");
        String databaseName = dbProperties.getProperty("databaseName");
        String user = dbProperties.getProperty("serverUsername");
        String pass = dbProperties.getProperty("serverPassword");

        return "jdbc:sqlserver://" + serverName + ";databaseName=" + databaseName +
                ";user=" + user + ";password=" + pass + ";encrypt=true;trustServerCertificate=true;";
    }

    public void closeConnection() {
        if (this.connection != null) {
            try {
                System.out.println("Closing the database connection!!");
                this.connection.close();
            } catch (SQLException e) {
                System.err.println("Failed to close the database connection: " + e.getMessage());
            }
        }
    }
}
