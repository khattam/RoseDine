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
        return this.connection;
    }

    public void closeConnection() {
        if (this.connection != null) {
            try {
                System.out.println("I am closing!!");
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to close the database connection.");
            }
        }
    }
}
