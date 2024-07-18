package org.example.foodorderingsystem.service;

;
import org.example.foodorderingsystem.dtos.MenuItemDTO;
import org.example.foodorderingsystem.mapper.EntityToDTOMapper;
import org.example.foodorderingsystem.model.MenuItem;
import org.example.foodorderingsystem.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    public MenuItemDTO addMenuItem(MenuItem menuItem) {
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return EntityToDTOMapper.toMenuItemDTO(savedMenuItem);
    }

    public MenuItemDTO getMenuItemById(Long id) {
        Optional<MenuItem> menuItem = menuItemRepository.findById(id);
        return menuItem.map(EntityToDTOMapper::toMenuItemDTO).orElse(null);
    }

    public List<MenuItemDTO> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .map(EntityToDTOMapper::toMenuItemDTO)
                .collect(Collectors.toList());
    }

    public List<MenuItemDTO> getAllAvailableMenuItems() {
        return menuItemRepository.findAll().stream()
                .filter(menuItem -> menuItem.getTotalAvailableQuantity() > 0)
                .map(EntityToDTOMapper::toMenuItemDTO)
                .collect(Collectors.toList());
    }
}

