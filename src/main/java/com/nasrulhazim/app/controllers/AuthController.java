package com.nasrulhazim.app.controllers;

import com.nasrulhazim.app.models.User;
import com.nasrulhazim.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;

        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/sign-up")
    public void store(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
