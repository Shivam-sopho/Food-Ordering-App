package org.example.foodorderingsystem.controller;


import org.example.foodorderingsystem.dtos.UserDTO;
import org.example.foodorderingsystem.exceptions.BadRequestException;
import org.example.foodorderingsystem.exceptions.ResourceNotFoundException;
import org.example.foodorderingsystem.mapper.EntityToDTOMapper;
import org.example.foodorderingsystem.model.Session;
import org.example.foodorderingsystem.model.User;
import org.example.foodorderingsystem.service.SessionService;
import org.example.foodorderingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @PostMapping("/login")
    public Session login(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        Optional<User> optionalUser = userService.findUserByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (userService.checkPassword(user, password)) {
                HttpSession session = request.getSession(true); // Create a new session
                session.setAttribute("user", user);
                sessionService.registerSession(user.getId(), session.getId());
                return new Session(session.getId()); // Return session ID as a JSON object
            } else {
                throw new BadRequestException("Invalid password");
            }
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }

    @PostMapping("/validate")
    public UserDTO validateSession(@RequestParam String sessionId, HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Do not create a new session
        if (session != null && session.getId().equals(sessionId)) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                return EntityToDTOMapper.toUserDTO(user);
            } else {
                throw new BadRequestException("Invalid session");
            }
        } else {
            throw new BadRequestException("Session not found");
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                sessionService.removeSession(user.getId(), session.getId());
            }
            session.invalidate();
            return "Logged out successfully";
        } else {
            throw new BadRequestException("No active session found");
        }
    }

    @GetMapping("/logout-all")
    public String logoutAll(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                Set<String> sessions = sessionService.getSessions(user.getId());
                for (String sessionId : sessions) {
                    HttpSession userSession = request.getSession(false);
                    if (userSession != null && userSession.getId().equals(sessionId)) {
                        userSession.invalidate();
                    }
                }
                sessionService.removeAllSessions(user.getId());
            }
            session.invalidate();
            return "Logged out from all sessions successfully";
        } else {
            throw new BadRequestException("No active session found");
        }
    }

    @GetMapping("/current-user")
    public UserDTO currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                return EntityToDTOMapper.toUserDTO(user);
            } else {
                throw new ResourceNotFoundException("No user found in the current session");
            }
        } else {
            throw new BadRequestException("No active session found");
        }
    }
}
