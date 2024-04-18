package db.services;

import java.sql.*;
import java.time.LocalDate;

public class MenuService {

    private DatabaseConnectionService dbService;

    public MenuService(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }



    public boolean addMenu(LocalDate date, String type, String timings) {
        String SQL = "{call dbo.InsertMenu(?, ?, ?, ?)}";
        Connection conn = null;
        CallableStatement cstmt = null;

        try {
            conn = dbService.getConnection();
            cstmt = conn.prepareCall(SQL);
            cstmt.setDate(1, Date.valueOf(date));
            cstmt.setString(2, type);
            cstmt.setInt(3, 1); // Use the correct Rest_ID based on your restaurant table
            cstmt.setString(4, timings); // Set the default value for Timings if null
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
        } finally {
            try {
                if (cstmt != null) cstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean addMenuItem(String itemName) {
        Connection conn = null;
        try {
            conn = dbService.getConnection();

            // First, get a new Macro ID
            int macroId = getOrInsertMacroId(conn);  // Ensures a Macro ID is retrieved

            if (macroId <= 0) {
                System.out.println("Failed to retrieve or create a new macro entry.");
                return false;
            }

            String SQL = "{call dbo.InsertMenuItem(?, ?)}";
            try (CallableStatement cstmt = conn.prepareCall(SQL)) {
                cstmt.setString(1, itemName);
                cstmt.setInt(2, macroId);  // Pass the macro ID as an input parameter
                int affectedRows = cstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Inserted menu item successfully with Macro ID: " + macroId);
                    return true;
                } else {
                    System.out.println("Inserting menu item failed, no rows affected.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int getOrInsertMacroId(Connection conn) throws SQLException {
        String SQL = "{call dbo.InsertMacro(?)}";
        try (CallableStatement cstmt = conn.prepareCall(SQL)) {
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.execute();
            return cstmt.getInt(1);  // Retrieve the output parameter value
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;  // Return an invalid ID if an error occurs
        }
    }


}
