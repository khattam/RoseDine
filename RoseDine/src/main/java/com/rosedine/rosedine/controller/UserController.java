package com.rosedine.rosedine.controller;

import com.rosedine.rosedine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

//    @PostMapping("/register")
//    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> user) {
//        System.out.println("Attempted Registraiton");
//        try {
//            // Validate input
//            if (user == null || !user.containsKey("email") || !user.containsKey("password")) {
//                return ResponseEntity.badRequest().body("Invalid request body");
//            }
//
//            String email = user.get("email");
//            String password = user.get("password");
//
//            if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
//                return ResponseEntity.badRequest().body("Email and password cannot be empty");
//            }
//
//            if (userService.userExists(email)) {
//                return ResponseEntity.status(409).body("User already exists");
//            }
//
//            userService.createUser(email, password);
//            return ResponseEntity.ok("User registered successfully");
//        } catch (Exception e) {
//            // Handle unexpected errors
//            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
//        }
//    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> user) {
        System.out.println("Attempted Registration");
        try {
            if (user == null || !user.containsKey("email") || !user.containsKey("password")
                    || !user.containsKey("fname") || !user.containsKey("lname")) {
                return ResponseEntity.badRequest().body("Invalid request body");
            }

            String fname = user.get("fname");
            String lname = user.get("lname");
            String email = user.get("email");
            String password = user.get("password");

            if (email.isEmpty() || password.isEmpty() || fname.isEmpty() || lname.isEmpty()) {
                return ResponseEntity.badRequest().body("All fields must be filled");
            }

            if (userService.userExists(email)) {
                return ResponseEntity.status(409).body("User already exists");
            }

            userService.createUser(fname, lname, email, password);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
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

            int userId = userService.validateUser(email, password);

            if (userId != -1) {
                return ResponseEntity.ok(String.valueOf(userId));
            } else {
                return ResponseEntity.status(401).body("Invalid email or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(STR."Internal server error: \{e.getMessage()}");
        }
    }
}
