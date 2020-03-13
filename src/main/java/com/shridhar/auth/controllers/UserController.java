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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class UserController {

    private UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @ApiOperation(value = "Return list of users", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user list"),
            @ApiResponse(code = 500, message = "Internal error while processing the request")
    })
    private List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @ApiOperation(value = "Return the user by Id", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user object"),
            @ApiResponse(code = 500, message = "Internal error while processing the request")
    })
    @GetMapping("/users/{id}")
    private User getUser(@PathVariable("id") int id) {
        return userService.getPersonById(id);
    }

    @ApiOperation(value = "Delete the user by Id", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted the user"),
            @ApiResponse(code = 500, message = "Internal error while processing the request")
    })
    @DeleteMapping("/users/{id}")
    private void deleteUser(@PathVariable("id") int id) {
        userService.delete(id);
    }

    @ApiOperation(value = "Update the user by Id", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user list"),
            @ApiResponse(code = 500, message = "Internal error while processing the request")
    })
    @PostMapping("/users/{id}")
    private User updateUser(@RequestBody UserWithPassword user) {
        return userService.saveOrUpdate(user);
    }

    @ApiOperation(value = "User sign up operation", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created user"),
            @ApiResponse(code = 500, message = "Internal error while processing the request")
    })
    @PutMapping("/users")
    private User createUser(@RequestBody UserWithPassword user) {
        return userService.saveOrUpdate(user);
    }
    
}
