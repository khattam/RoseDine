package com.rosedine.rosedine.controller;

import com.rosedine.rosedine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> user) {
        System.out.println("Attempted Registraiton");
        try {
            // Validate input
            if (user == null || !user.containsKey("email") || !user.containsKey("password")) {
                return ResponseEntity.badRequest().body("Invalid request body");
            }

            String email = user.get("email");
            String password = user.get("password");

            if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
                return ResponseEntity.badRequest().body("Email and password cannot be empty");
            }

            if (userService.userExists(email)) {
                return ResponseEntity.status(409).body("User already exists");
            }

            userService.createUser(email, password);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Map<String, String> user) {
        try {
            if (user == null || !user.containsKey("email") || !user.containsKey("password")) {
                return ResponseEntity.badRequest().body("Invalid request body");
            }

            String email = user.get("email");
            String password = user.get("password");

            boolean valid = userService.validateUser(email, password);

            if (valid) {
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.status(401).body("Invalid email or password");
            }
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }
}
