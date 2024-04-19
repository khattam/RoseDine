package db.services;

import java.sql.*;

public class MenuService {

    private DatabaseConnectionService dbService;

    public MenuService(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    public int addMenu(Date date, String timings, String type, int restID) {
        String SQL = "{call dbo.InsertMenu(?, ?, ?, ?)}";
        try (Connection conn = dbService.getConnection();
             CallableStatement cstmt = conn.prepareCall(SQL)) {
            cstmt.setDate(1, date);
            cstmt.setString(2, timings);
            cstmt.setString(3, type);
            cstmt.setInt(4, restID);

            ResultSet rs = cstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during insertMenu operation: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public int addMenuItem(String name, int macroID, int dietaryRestrictionsID, int overallStars) {
        String SQL = "{call dbo.InsertMenuItem(?, ?, ?, ?)}";
        Connection conn = dbService.getConnection();
        try (
             CallableStatement cstmt = conn.prepareCall(SQL)) {
            cstmt.setString(1, name);
            cstmt.setInt(2, macroID);
            cstmt.setInt(3, dietaryRestrictionsID);
            cstmt.setInt(4, overallStars);

            ResultSet rs = cstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during insertMenuItem operation: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public boolean addIncludesItem(int menuID, int menuItemID) {
        String SQL = "{call dbo.InsertIncludesItem(?, ?)}";
        try (Connection conn = dbService.getConnection();
             CallableStatement cstmt = conn.prepareCall(SQL)) {
            cstmt.setInt(1, menuID);
            cstmt.setInt(2, menuItemID);
            cstmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("SQL Error during insertIncludesItem operation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}