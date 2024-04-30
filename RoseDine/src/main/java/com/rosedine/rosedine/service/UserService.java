package com.rosedine.rosedine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean userExists(String email) {
        try {
            String query = "SELECT COUNT(*) FROM [User]  WHERE [Email ID] = ?";
            Integer count = jdbcTemplate.queryForObject(query, new Object[]{email}, Integer.class);
            return count != null && count > 0;
        } catch (Exception e) {

            return false; // Return false if query fails
        }
    }

    public void createUser(String email, String password) {
        try {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // Insert into DietaryRestrictions
            String insertDietaryRestriction = "INSERT INTO dbo.DietaryRestrictions (Is_Vegan, Is_Vegetarian, Is_Gluten_Free) VALUES (0, 0, 0)";
            jdbcTemplate.update(insertDietaryRestriction);

            Long dietaryRestrictionId = jdbcTemplate.queryForObject("SELECT SCOPE_IDENTITY()", Long.class);

            // Insert into User
            String insertUser = "INSERT INTO [User] ([Email ID], Password, DietaryRestrictions_ID) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertUser, email, hashedPassword, dietaryRestrictionId);
        } catch (Exception e) {
            // Handle database and SQL errors
            System.err.println("Error creating user: " + e.getMessage());
        }
    }

    public boolean validateUser(String email, String password) {
        try {
            String query = "SELECT Password FROM [User]  WHERE [Email ID] = ?";
            String storedHashedPassword = jdbcTemplate.queryForObject(query, new Object[]{email}, String.class);

            return storedHashedPassword != null && BCrypt.checkpw(password, storedHashedPassword);
        } catch (Exception e) {
            // Handle database and query errors
            return false; // Return false if query fails or validation fails
        }
    }
}
