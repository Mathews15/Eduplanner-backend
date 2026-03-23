package com.eduplan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduplan.model.User;
import com.eduplan.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    // Register user
    public User registerUser(User user) {

        if(user.getEmail() == null || user.getPassword() == null){
            throw new RuntimeException("Email or Password is missing");
        }

        if(userRepository.findByEmail(user.getEmail()) != null){
            throw new RuntimeException("Email already exists");
        }

        return userRepository.save(user);
    }

    // Login user
    public User login(String email, String password) {

        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new RuntimeException("User not found");
        }

        if(!user.getPassword().equals(password)){
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}