package org.example.foodorderingsystem.service;

;
import org.example.foodorderingsystem.dtos.MenuItemDTO;
import org.example.foodorderingsystem.exceptions.ResourceNotFoundException;
import org.example.foodorderingsystem.mapper.DTOToEntityMapper;
import org.example.foodorderingsystem.mapper.EntityToDTOMapper;
import org.example.foodorderingsystem.model.MenuItem;
import org.example.foodorderingsystem.repository.MenuItemRepository;
import org.example.foodorderingsystem.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;
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

    public MenuItemDTO createMenuItem( MenuItemDTO menuItemDTO) {
        MenuItem menuItem = DTOToEntityMapper.getMenuItem(menuItemDTO);
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return EntityToDTOMapper.toMenuItemDTO(savedMenuItem);
    }

    public MenuItem getMenuItem(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found"));
    }

    public List<MenuItem> getMenuItems(List<Long> ids){
        return menuItemRepository.findAllById(ids);
    }
}

