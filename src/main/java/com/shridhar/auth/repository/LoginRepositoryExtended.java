package com.shridhar.auth.repository;

import com.shridhar.auth.dao.RoleDAO;
import com.shridhar.auth.dao.UserDAO;

public interface LoginRepositoryExtended {
    
	public UserDAO login(String email);
    
    public RoleDAO getRole(Integer id);
}