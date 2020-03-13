package com.shridhar.auth.service;

import static com.shridhar.auth.constants.Constants.ROLE_USER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.shridhar.auth.dao.RoleDAO;
import com.shridhar.auth.dao.UserDAO;
import com.shridhar.auth.exception.UserRegistrationException;
import com.shridhar.auth.model.User;
import com.shridhar.auth.model.UserWithPassword;
import com.shridhar.auth.repository.RoleRepository;
import com.shridhar.auth.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	private DozerBeanMapper mapper;

	public UserService() {
		this.mapper = new DozerBeanMapper();
	}

	@Cacheable("users")
	//@HystrixCommand(fallbackMethod = "defaultUsers")
	public List<User> getAllPersons() {

		List<User> users = new ArrayList<>();

		userRepository.findAll().forEach(user -> users.add(mapper.map(user, User.class)));

		return users;
	}

	public User getPersonById(int id) {
		return mapper.map(userRepository.findById(id).get(), User.class);
	}

	@CacheEvict(value = "users", allEntries = true)
	public User saveOrUpdate(UserWithPassword user) {

		User userFromDb = null;

		String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		user.setPassword(hashedPassword);

		try {
			// Populate the User DAO form incoming data.
			UserDAO userDAO = mapper.map(user, UserDAO.class);
			// Attach the role to the user.
			RoleDAO userRole = roleRepository.findByName(ROLE_USER);
			userDAO.setRoles(Arrays.asList(userRole));
			// Persist the user
			userRepository.save(userDAO);

			if (userDAO != null) {
				userFromDb = this.mapper.map(userDAO, User.class);
			}

		} catch (org.hibernate.exception.ConstraintViolationException | ConstraintViolationException cve) {
			throw new UserRegistrationException("User registration validation failure: " + cve.getMessage());
		} catch (DataIntegrityViolationException div) {
			throw new UserRegistrationException("User email taken: " + div.getMessage());
		}

		return userFromDb;
	}

	@CacheEvict(value = "users", key = "#id")
	public void delete(int id) {
		userRepository.deleteById(id);
	}

	@SuppressWarnings("unused")
	private List<User> defaultUsers() {
		return new ArrayList<User>();
	}

}
