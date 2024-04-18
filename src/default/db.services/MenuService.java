//package db.services;
//
//import java.sql.*;
//import java.time.LocalDate;
//
//public class MenuService {
//
//    private DatabaseConnectionService dbService;
//
//    public MenuService(DatabaseConnectionService dbService) {
//        this.dbService = dbService;
//    }
//
//    public boolean addMenu(LocalDate date, String type, String timings) {
//        String SQL = "{call dbo.InsertMenu(?, ?, ?, ?)}";
//        Connection conn = null;
//        CallableStatement cstmt = null;
//
//        try {
//            conn = dbService.getConnection();
//            cstmt = conn.prepareCall(SQL);
//            cstmt.setDate(1, Date.valueOf(date));
//            cstmt.setString(2, type);
//            cstmt.setInt(3, 1); // Use the correct Rest_ID based on your restaurant table
//            cstmt.setString(4, timings); // Set the default value for Timings if null
//            int affectedRows = cstmt.executeUpdate();
//
//            if (affectedRows > 0) {
//                System.out.println("Inserted menu successfully.");
//                return true;
//            } else {
//                System.out.println("Inserting menu failed, no rows affected.");
//                return false;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        } finally {
//            try {
//                if (cstmt != null) cstmt.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public boolean addMenuItem(String itemName) {
//        String SQL = "{call InsertMenuItemWithNewFKs(?)}";
//        try (Connection conn = dbService.getConnection();
//             CallableStatement cstmt = conn.prepareCall(SQL)) {
//
//            conn.setAutoCommit(false); // Begin transaction
//
//            cstmt.setString(1, itemName);
//            int affectedRows = cstmt.executeUpdate();
//
//            if (affectedRows > 0) {
//                System.out.println("Menu item inserted successfully.");
//                conn.commit(); // Commit transaction
//                return true;
//            } else {
//                System.out.println("No menu item was inserted.");
//                conn.rollback(); // Rollback transaction
//                return false;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//}
//

////    public boolean addMenuItem(String itemName) {
////        Connection conn = null;
////        try {
////            conn = dbService.getConnection();
////            conn.setAutoCommit(false); // Start transaction
////
////            // Insert macro and dietary restrictions, and retrieve their IDs
////            int macroId = insertMacro(conn);
////            if (macroId == -1) {
////                conn.rollback();
////                return false;
////            }
////
////            int dietaryRestrictionsId = insertDietaryRestrictions(conn);
////            if (dietaryRestrictionsId == -1) {
////                conn.rollback();
////                return false;
////            }
////
////            // Insert the menu item with obtained IDs
////            if (!insertMenuItem(conn, itemName, macroId, dietaryRestrictionsId)) {
////                conn.rollback();
////                return false;
////            }
////
////            conn.commit();  // Commit transaction if all operations succeed
////            return true;
////        } catch (SQLException e) {
////            try {
////                if (conn != null) conn.rollback();
////            } catch (SQLException ex) {
////                ex.printStackTrace();
////            }
////            e.printStackTrace();
////            return false;
////        } finally {
////            if (conn != null) {
////                try {
////                    conn.setAutoCommit(true);
////                    conn.close();
////                } catch (SQLException e) {
////                    e.printStackTrace();
////                }
////            }
////        }
////    }
////
////    private int insertMacro(Connection conn) throws SQLException {
////        String sql = "{call dbo.InsertMacro(?)}";
////        try (CallableStatement cstmt = conn.prepareCall(sql)) {
////            cstmt.registerOutParameter(1, Types.INTEGER);
////            cstmt.execute();
////            return cstmt.getInt(1);
////        } catch (SQLException e) {
////            e.printStackTrace();
////            return -1;
////        }
////    }
////
////    private int insertDietaryRestrictions(Connection conn) throws SQLException {
////        String sql = "{call dbo.InsertDietaryRestrictions(?)}";
////        try (CallableStatement cstmt = conn.prepareCall(sql)) {
////            cstmt.registerOutParameter(1, Types.INTEGER);
////            cstmt.execute();
////            return cstmt.getInt(1);
////        } catch (SQLException e) {
////            e.printStackTrace();
////            return -1;
////        }
////    }
////
////    private boolean insertMenuItem(Connection conn, String itemName, int macroId, int dietaryRestrictionsId) throws SQLException {
////        String sql = "{call dbo.InsertMenuItem(?, ?, ?)}";
////        try (CallableStatement cstmt = conn.prepareCall(sql)) {
////            cstmt.setString(1, itemName);
////            cstmt.setInt(2, macroId);
////            cstmt.setInt(3, dietaryRestrictionsId);
////            return cstmt.executeUpdate() > 0;
////        } catch (SQLException e) {
////            e.printStackTrace();
////            return false;
////        }
////    }
////}

package db.services;

import java.sql.*;

public class MenuService {

    private DatabaseConnectionService dbService;

    public MenuService(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    public boolean addMenu(String date, String type, String timings) {
        Connection conn = null;
        CallableStatement cstmt = null;
        try {
            conn = dbService.getConnection();
            String SQL = "{call dbo.InsertMenu(?, ?, ?, ?)}";
            cstmt = conn.prepareCall(SQL);
            cstmt.setDate(1, Date.valueOf(date));
            cstmt.setString(2, type);
            cstmt.setInt(3, 1); // Assuming 1 is the correct Rest_ID
            cstmt.setString(4, timings);
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
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean addMenuItem(String itemName) {
        Connection conn = null;
        try {
            conn = dbService.getConnection();
            conn.setAutoCommit(false);

            int macroId = getOrInsertMacroId(conn);
            int dietaryRestrictionsId = getOrInsertDietaryRestrictionsId(conn);

            String SQL = "{call dbo.InsertMenuItem(?, ?, ?)}";
            try (CallableStatement cstmt = conn.prepareCall(SQL)) {
                cstmt.setString(1, itemName);
                cstmt.setInt(2, macroId);
                cstmt.setInt(3, dietaryRestrictionsId);
                int affectedRows = cstmt.executeUpdate();
                if (affectedRows > 0) {
                    conn.commit();
                    System.out.println("Inserted menu item successfully.");
                    return true;
                } else {
                    conn.rollback();
                    System.out.println("Inserting menu item failed.");
                    return false;
                }
            }
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private int getOrInsertMacroId(Connection conn) throws SQLException {
        String checkSQL = "SELECT TOP 1 ID FROM Macros";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(checkSQL)) {
            if (rs.next()) {
                return rs.getInt("ID");
            } else {
                String insertSQL = "INSERT INTO Macros (Fats, Protein, Net_Carbs, Calories, Total_Carbs) VALUES (0, 0, 0, 0, 0)";
                stmt.executeUpdate(insertSQL, Statement.RETURN_GENERATED_KEYS);
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating macro failed, no ID obtained.");
                    }
                }
            }
        }
    }

    private int getOrInsertDietaryRestrictionsId(Connection conn) throws SQLException {
        String checkSQL = "SELECT TOP 1 ID FROM DietaryRestrictions";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(checkSQL)) {
            if (rs.next()) {
                return rs.getInt("ID");
            } else {
                String insertSQL = "INSERT INTO DietaryRestrictions (Is_Vegan, Is_Vegetarian, Is_Gluten_Free) VALUES (0, 0, 0)";
                stmt.executeUpdate(insertSQL, Statement.RETURN_GENERATED_KEYS);
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating dietary restriction failed, no ID obtained.");
                    }
                }
            }
        }
        }
}
