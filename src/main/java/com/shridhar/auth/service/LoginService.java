package com.shridhar.auth.service;

import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.shridhar.auth.controllers.LoginController;
import com.shridhar.auth.dao.UserDAO;
import com.shridhar.auth.events.LoginEventPublisher;
import com.shridhar.auth.exception.LoginFailedException;
import com.shridhar.auth.model.LoginData;
import com.shridhar.auth.model.LoginResponse;
import com.shridhar.auth.model.LoginUser;
import com.shridhar.auth.model.User;
import com.shridhar.auth.repository.LoginRepository;

import io.micrometer.core.annotation.Timed;

@Service
public class LoginService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	private LoginRepository loginRepository;

	private ClientService clientService;

	private TokenService tokenService;

	private DozerBeanMapper mapper;
	
	private LoginEventPublisher loginEventPublisher;

	public LoginService(@Autowired LoginRepository loginRepository, @Autowired ClientService clientService,
			@Autowired TokenService tokenService, @Autowired LoginEventPublisher loginEventPublisher) {
		this.loginRepository = loginRepository;
		this.clientService = clientService;
		this.tokenService = tokenService;
		this.loginEventPublisher = loginEventPublisher;
		
		this.mapper = new DozerBeanMapper();
	}

	@Timed("login")
	public LoginResponse login(LoginUser loginUser) throws IllegalAccessException {

		LoginResponse loginResponse = null;

		if (loginUser != null) {
			String clientId = loginUser.getClientId();
			if (clientId != null && !clientId.isEmpty()) {
				if (clientService.getAllClients().stream().filter(item -> clientId.equals(item.getClientkey()))
						.collect(Collectors.toList()).size() == 0)
					throw new IllegalAccessException("ClientId is not permitted");
			}

			LoginData loginData = loginUser.getLoginData();

			if (loginData != null) {

				String rawPassword = loginData.getPassword();
				String email = loginData.getEmail();
				boolean status = true;

				UserDAO userDto = loginRepository.login(email);

				// Log the reason separately for tracking purpose.
				// 1. Email not found
				if (userDto == null) {
					LOGGER.error("User email not found: {}", email);
					status = false;
				} else if (!BCrypt.checkpw(rawPassword, userDto.getPassword())) {
					// 2. Incorrect password
					LOGGER.error("Incorrect password for: {} ", email);
					status = false;
				}
				
				loginEventPublisher.publishEvent(email, status);
				
				if (!status) {
					throw new LoginFailedException("Login failed for " + email);
				}

				User user = mapper.map(userDto, User.class);
				
				loginResponse = tokenService.getToken(user, ScopeType.USER, clientId);
			}
		}
		return loginResponse;
	}

}
