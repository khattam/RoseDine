package com.rosedine.rosedine.controller;

import com.rosedine.rosedine.dto.UserPreferences;
import com.rosedine.rosedine.repository.UserRepository;
import com.rosedine.rosedine.service.UserPreferencesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user-preferences")
public class UserPreferencesController {

    @Autowired
    private UserPreferencesService userPreferencesService;

    @PutMapping("/update-dietary-restriction")
    public ResponseEntity<String> updateDietaryRestriction(@RequestParam("userId") int userId,
                                                           @RequestParam("restrictionName") String restrictionName,
                                                           @RequestParam("restrictionValue") boolean restrictionValue) {
        try {
            userPreferencesService.updateDietaryRestriction(userId, restrictionName, restrictionValue);
            return ResponseEntity.ok("Dietary restriction updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update dietary restriction: " + e.getMessage());
        }
    }

    @PutMapping("/update-macro")
    public ResponseEntity<String> updateMacro(@RequestParam("userId") int userId,
                                              @RequestParam("mealType") String mealType,
                                              @RequestParam("macroName") String macroName,
                                              @RequestParam("macroValue") int macroValue) {
        try {
            userPreferencesService.updateMacro(userId, mealType, macroName, macroValue);
            return ResponseEntity.ok("Macro updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update macro: " + e.getMessage());
        }
    }

    @GetMapping("/get-preferences")
    public ResponseEntity<Map<String, Object>> getUserPreferences(@RequestParam("userId") int userId) {
        try {
            Map<String, Object> preferences = userPreferencesService.getUserPreferences(userId);
            return ResponseEntity.ok(preferences);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    @GetMapping("/get-all-preferences")
    public ResponseEntity<Map<String, Object>> getAllPreferences(@RequestParam("userId") int userId, @RequestParam String mealType) {
        try {
            // Call the new method from the service
            Map<String, Object> preferences = userPreferencesService.getAllUserPreferences(userId, mealType);

            // Respond with the result
            return ResponseEntity.ok(preferences);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}



