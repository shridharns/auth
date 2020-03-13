package com.shridhar.auth.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shridhar.auth.model.LoginResponse;
import com.shridhar.auth.model.LoginUser;
import com.shridhar.auth.model.User;
import com.shridhar.auth.service.LoginService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    private LoginService loginService;

    public LoginController(@Autowired LoginService loginService) {
        this.loginService = loginService;
        LOGGER.debug("Login controller initialized");
    }

    @ApiOperation(value = "Login method for users, with email and password", response = LoginResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Login response, including token, scope, expiresIn"),
            @ApiResponse(code = 500, message = "Internal error while processing the request")
    })
    @RequestMapping(path = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponse login(@RequestBody LoginUser loginUser) throws IllegalAccessException {
    
        return loginService.login(loginUser);
    }

}
