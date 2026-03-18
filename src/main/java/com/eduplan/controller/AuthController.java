package com.eduplan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.eduplan.model.User;
import com.eduplan.service.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "https://eduplanner-frontend.onrender.com/")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Register user
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return authService.registerUser(user);
    }

    // Login user
    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return authService.login(user.getEmail(), user.getPassword());
    }
}