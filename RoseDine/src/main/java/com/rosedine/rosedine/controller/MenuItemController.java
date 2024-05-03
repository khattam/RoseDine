package com.rosedine.rosedine.controller;

import com.rosedine.rosedine.dto.MenuItemDTO;
import com.rosedine.rosedine.service.MenuItemService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {
    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public List<MenuItemDTO> getMenuItemsByDateAndType(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("type") String type) {
        return menuItemService.getMenuItemsByDateAndType(date, type);
    }

    @GetMapping("/hardcoded")
    public List<MenuItemDTO> getHardcodedItems() {
        List<MenuItemDTO> hardcodedItems = List.of(
                new MenuItemDTO("Sandwich Veg", 5, 10, 7, 35, 250, false, true, false),
                new MenuItemDTO("Sandwich Non-Veg", 5, 20, 10, 30, 350, false, false, false),
                new MenuItemDTO("Salad", 5, 5, 5, 10, 150, true, true, true)
        );
        return hardcodedItems;
    }



}
