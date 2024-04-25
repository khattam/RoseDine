package com.rosedine.rosedine.service;

import com.rosedine.rosedine.dto.MenuItemDTO;
import com.rosedine.rosedine.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItemDTO> getMenuItemsByDateAndType(LocalDate date, String type) {
        return menuItemRepository.getMenuItemsByDateAndType(date, type);
    }
}