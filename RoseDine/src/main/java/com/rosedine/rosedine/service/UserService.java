package com.rosedine.rosedine.service;

import com.rosedine.rosedine.dto.UserPreferences;
import com.rosedine.rosedine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository; // Field for UserRepository

    @Autowired // Constructor injection for UserRepository
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean userExists(String email) {
        try {
            String query = "SELECT COUNT(*) FROM [User] WHERE [Email ID] = ?";
            Integer count = jdbcTemplate.queryForObject(query, new Object[]{email}, Integer.class);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public void createUser(String fname, String lname, String email, String password) {
        try {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // Insert dietary restrictions
            String insertDietaryRestriction = "INSERT INTO dbo.DietaryRestrictions (Is_Vegan, Is_Vegetarian, Is_Gluten_Free) VALUES (0, 0, 0)";
            jdbcTemplate.update(insertDietaryRestriction);

            // Retrieve the inserted dietary restriction ID
            Long dietaryRestrictionId = jdbcTemplate.queryForObject("SELECT SCOPE_IDENTITY()", Long.class);

            // Insert user with dietary restriction ID
            String insertUser = "INSERT INTO [User] ([Fname], [Lname], [Email ID], Password, DietaryRestrictions_ID) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(insertUser, fname, lname, email, hashedPassword, dietaryRestrictionId);
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
        }
    }

    public int validateUser(String email, String password) {
        try {
            String query = "SELECT UserID, Password FROM [User] WHERE [Email ID] = ?";
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

    public UserPreferences getUserPreferences(int userId, String mealType) {
        return userRepository.getUserPreferences(userId, mealType);
    }


}
