package com.rosedine.rosedine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Map;

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

            return false;
        }
    }

    public void createUser(String email, String password) {
        try {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());


            String insertDietaryRestriction = "INSERT INTO dbo.DietaryRestrictions (Is_Vegan, Is_Vegetarian, Is_Gluten_Free) VALUES (0, 0, 0)";
            jdbcTemplate.update(insertDietaryRestriction);

            Long dietaryRestrictionId = jdbcTemplate.queryForObject("SELECT SCOPE_IDENTITY()", Long.class);


            String insertUser = "INSERT INTO [User] ([Email ID], Password, DietaryRestrictions_ID) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertUser, email, hashedPassword, dietaryRestrictionId);
        } catch (Exception e) {

            System.err.println("Error creating user: " + e.getMessage());
        }
    }

    public int validateUser(String email, String password) {
        try {
            String query = "SELECT UserID, Password FROM [User]  WHERE [Email ID] = ?";
            Map<String, Object> result = jdbcTemplate.queryForMap(query, email);

            int userId = (int) result.get("UserID");
            String storedHashedPassword = (String) result.get("Password");

            if (storedHashedPassword != null && BCrypt.checkpw(password, storedHashedPassword)) {
                return userId;
            } else {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }
    }


}
