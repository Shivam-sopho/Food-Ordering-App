package org.example.foodorderingsystem.controller;


import org.example.foodorderingsystem.dtos.MenuItemDTO;
import org.example.foodorderingsystem.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu-items")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @GetMapping("/available")
    public List<MenuItemDTO> getAllAvailableMenuItems() {
        return menuItemService.getAllAvailableMenuItems();
    }
}
