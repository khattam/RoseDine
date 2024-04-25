package com.rosedine.rosedine.controller;

import com.rosedine.rosedine.service.JsonDataService;
import com.rosedine.rosedine.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final JsonDataService jsonDataService;
    private final MenuService menuService;

    public MenuController(@Autowired MenuService menuService, @Autowired JsonDataService jsonDataService) {
        this.menuService = menuService;
        this.jsonDataService = jsonDataService;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importJsonData() {
        try {
            jsonDataService.parseAndSaveJsonData();
            return ResponseEntity.ok("JSON data imported successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import JSON data.");
        }
    }

}