package org.example.foodorderingsystem.controller;

import org.example.foodorderingsystem.dtos.UserDTO;
import org.example.foodorderingsystem.model.Customer;
import org.example.foodorderingsystem.model.RestaurantOwner;
import org.example.foodorderingsystem.model.User;
import org.example.foodorderingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody Customer user) {
        return userService.registerUser(user);
    }

    @GetMapping("/{username}")
    public UserDTO getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @PostMapping("/registerRestaurantOwner")
    public UserDTO registerRestaurantOwner(@RequestBody RestaurantOwner user) {
        return userService.registerUser(user);
    }

    @GetMapping("/restaurantOwner/{username}")
    public UserDTO getRestaurantOwner(@PathVariable String username) {
        return userService.findByUsername(username);
    }

}

