package com.shridhar.auth.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shridhar.auth.model.User;
import com.shridhar.auth.model.UserWithPassword;
import com.shridhar.auth.service.UserService;

@RestController
public class UserController {

    UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    private List<User> getAllUsers() {
        return userService.getAllPersons();
    }

    @GetMapping("/users/{id}")
    private User getUser(@PathVariable("id") int id) {
        return userService.getPersonById(id);
    }

    @DeleteMapping("/users/{id}")
    private void deleteUser(@PathVariable("id") int id) {
        userService.delete(id);
    }

    @PostMapping("/users/{id}")
    private User updateUser(@RequestBody UserWithPassword user) {
        return userService.saveOrUpdate(user);
    }

    @PutMapping("/users")
    private User createUser(@RequestBody UserWithPassword user) {
        return userService.saveOrUpdate(user);
    }
    
}
