package com.rosedine.rosedine.service;

import com.rosedine.rosedine.dto.UserPreferences;
import com.rosedine.rosedine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired // Constructor injection for UserRepository
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void updateUserPassword(String email, String newPassword) {
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("UpdateUserPassword");

        SqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("EmailID", email)
                .addValue("NewPassword", hashedPassword);

        jdbcCall.execute(inParams);
    }


    public boolean userExists(String email) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("CheckUserExists2")
                .declareParameters(
                        new SqlParameter("Email", Types.NVARCHAR),
                        new SqlOutParameter("UserCount", Types.INTEGER));

        SqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("Email", email);

        Map<String, Object> result = jdbcCall.execute(inParams);
        return (Integer) result.get("UserCount") > 0;
    }

    public void createUser(String fname, String lname, String email, String password) {
        try {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            SimpleJdbcCall jdbcCallDR = new SimpleJdbcCall(jdbcTemplate)
                    .withProcedureName("InsertDietaryRestrictions2")
                    .declareParameters(new SqlOutParameter("ID", Types.INTEGER));

            SqlParameterSource inParamsDR = new MapSqlParameterSource()
                    .addValue("IsVegan", false)
                    .addValue("IsVegetarian", false)
                    .addValue("IsGlutenFree", false);

            Map<String, Object> outDR = jdbcCallDR.execute(inParamsDR);
            Integer dietaryRestrictionId = (Integer) outDR.get("ID");

            SimpleJdbcCall jdbcCallUser = new SimpleJdbcCall(jdbcTemplate)
                    .withProcedureName("InsertUser2");

            SqlParameterSource inParamsUser = new MapSqlParameterSource()
                    .addValue("Fname", fname)
                    .addValue("Lname", lname)
                    .addValue("EmailID", email)
                    .addValue("Password", hashedPassword)
                    .addValue("DietaryRestrictionsID", dietaryRestrictionId);

            jdbcCallUser.execute(inParamsUser);
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
        }
    }




    public int validateUser(String email, String password) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("ValidateUser")
                .declareParameters(
                        new SqlParameter("EmailID", Types.NVARCHAR),
                        new SqlOutParameter("UserID", Types.INTEGER),
                        new SqlOutParameter("HashedPassword", Types.NVARCHAR));

        SqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("EmailID", email);

        Map<String, Object> result = jdbcCall.execute(inParams);
        Integer userId = (Integer) result.get("UserID");
        String storedHashedPassword = (String) result.get("HashedPassword");

        if (userId != null && storedHashedPassword != null && BCrypt.checkpw(password, storedHashedPassword)) {
            return userId;
        } else {
            return -1;
        }
    }


    public UserPreferences getUserPreferences(int userId, String mealType) {
        return userRepository.getUserPreferences(userId, mealType);
    }
}
