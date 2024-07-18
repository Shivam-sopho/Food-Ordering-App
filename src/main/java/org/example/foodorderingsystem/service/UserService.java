package org.example.foodorderingsystem.service;

import org.example.foodorderingsystem.dtos.UserDTO;
import org.example.foodorderingsystem.mapper.EntityToDTOMapper;
import org.example.foodorderingsystem.model.User;
import org.example.foodorderingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO registerUser(User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        User savedUser = userRepository.save(user);
        return EntityToDTOMapper.toUserDTO(savedUser);
    }

    public UserDTO findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(EntityToDTOMapper::toUserDTO).orElse(null);
    }

    public boolean checkPassword(User user, String rawPassword) {
        return BCrypt.checkpw(rawPassword, user.getPassword());
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
