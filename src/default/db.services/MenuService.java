package db.services;

import java.sql.*;
import java.time.LocalDate;

public class MenuService {

    private DatabaseConnectionService dbService;

    public MenuService(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    public boolean addMenuItem(String itemName) {
        String SQL = "{call dbo.InsertMenuItem(?)}"; // Replace with your actual stored procedure name

        try (Connection conn = dbService.getConnection();
             CallableStatement cstmt = conn.prepareCall(SQL)) {

            cstmt.setString(1, itemName);
            int affectedRows = cstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Inserted menu item successfully.");
                return true;
            } else {
                System.out.println("Inserting menu item failed, no rows affected.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addMenu(LocalDate date, String type) {
        String SQL = "{call dbo.InsertMenu(?, ?)}";

        try (Connection conn = dbService.getConnection();
             CallableStatement cstmt = conn.prepareCall(SQL)) {

            cstmt.setDate(1, Date.valueOf(date));
            cstmt.setString(2, type);
            int affectedRows = cstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Inserted menu successfully.");
                return true;
            } else {
                System.out.println("Inserting menu failed, no rows affected.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
