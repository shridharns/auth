package com.shridhar.auth.repository;

import org.springframework.data.repository.CrudRepository;

import com.shridhar.auth.dao.UserDAO;

public interface UserRepository extends CrudRepository<UserDAO, Integer> {
	
	UserDAO findByEmail(String email);
}