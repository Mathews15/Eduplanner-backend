package com.eduplan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduplan.model.User;
import com.eduplan.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {

        if (user.getEmail() == null || user.getPassword() == null) {
            throw new RuntimeException("Email or Password is missing");
        }

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        // FIX 5: Never store plain password in production — at minimum clear it before saving
        // For now saving as-is since project has no password encoder wired
        return userRepository.save(user);
    }

    public User login(String email, String password) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        // FIX 5: Clear password before sending to frontend
        user.setPassword(null);
        return user;
    }
}