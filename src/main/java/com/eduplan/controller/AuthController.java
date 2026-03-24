package com.eduplan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eduplan.model.User;
import com.eduplan.service.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"https://eduplanner-frontend.onrender.com", "http://localhost:5173", "http://localhost:3000"})
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            return ResponseEntity.ok(authService.registerUser(user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            return ResponseEntity.ok(authService.login(user.getEmail(), user.getPassword()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}