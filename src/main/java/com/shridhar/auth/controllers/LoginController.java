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
import com.shridhar.auth.service.LoginService;

@RestController
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    private LoginService loginService;

    public LoginController(@Autowired LoginService loginService) {
        this.loginService = loginService;
        LOGGER.debug("Login controller initialized");
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponse login(@RequestBody LoginUser loginUser) throws IllegalAccessException {
    
        return loginService.login(loginUser);
    }

}
