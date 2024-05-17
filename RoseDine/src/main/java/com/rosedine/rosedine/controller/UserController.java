package com.rosedine.rosedine.controller;

import com.rosedine.rosedine.service.EmailService;
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

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> user) {
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

            String verificationToken = emailService.sendVerificationEmail(email);
            return ResponseEntity.ok("Please verify your email. A verification code has been sent to " + email + ". Token: " + verificationToken);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }


    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String code = request.get("code");
        String fname = request.get("fname");
        String lname = request.get("lname");
        String email = request.get("email");
        String password = request.get("password");

        if (emailService.validateVerificationToken(token, code)) {
            userService.createUser(fname, lname, email, password);
            return ResponseEntity.ok("Email verified successfully and user created.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token or code.");
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
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }
}
