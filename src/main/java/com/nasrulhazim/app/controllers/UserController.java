package com.nasrulhazim.app.controllers;

import com.nasrulhazim.app.models.User;
import com.nasrulhazim.app.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> index() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public User show(@PathVariable long id) {
        return userRepository.findOne(id);
    }

    @PostMapping("/users")
    public List<User> store(@RequestBody User user) {
        userRepository.save(user);
        return userRepository.findAll();
    }
}
