package com.shridhar.auth.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import com.shridhar.auth.constants.Constants;
import com.shridhar.auth.dao.PrivilegeDAO;
import com.shridhar.auth.dao.RoleDAO;
import com.shridhar.auth.dao.UserDAO;
import com.shridhar.auth.repository.PrivilegeRepository;
import com.shridhar.auth.repository.RoleRepository;
import com.shridhar.auth.repository.UserRepository;

@Component
@Profile("!test")
public class RolesPrivilegeLoader implements
  ApplicationListener<ContextRefreshedEvent> {
 
    boolean alreadySetup = false;
 
    @Autowired
    private UserRepository userRepository;
  
    @Autowired
    private RoleRepository roleRepository;
  
    @Autowired
    private PrivilegeRepository privilegeRepository;
  
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
  
        if (alreadySetup)
            return;
        
        UserDAO userDAO = userRepository.findByEmail("admin@test.com");
        
        if (userDAO != null) {
        	return;
        }
        
        PrivilegeDAO readPrivilege
          = createPrivilegeIfNotFound(Constants.READ_PRIVILEGE);
        PrivilegeDAO writePrivilege
          = createPrivilegeIfNotFound(Constants.WRITE_PRIVILEGE);
  
        List<PrivilegeDAO> adminPrivileges = Arrays.asList(
          readPrivilege, writePrivilege);        
        createRoleIfNotFound(Constants.ROLE_ADMIN, adminPrivileges);
        createRoleIfNotFound(Constants.ROLE_USER, Arrays.asList(readPrivilege));
 
        // Create Admin user if it's not present.
        RoleDAO adminRole = roleRepository.findByName(Constants.ROLE_USER);
        UserDAO user = new UserDAO();
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setPassword(BCrypt.hashpw("admin123", BCrypt.gensalt()));
        user.setEmail("admin@test.com");
        user.setRoles(Arrays.asList(adminRole));
        userRepository.save(user);
 
        alreadySetup = true;
    }
 
    @Transactional
    private PrivilegeDAO createPrivilegeIfNotFound(String name) {
  
    	PrivilegeDAO privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new PrivilegeDAO(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }
 
    @Transactional
    private RoleDAO createRoleIfNotFound(
      String name, Collection<PrivilegeDAO> privileges) {
  
    	RoleDAO role = roleRepository.findByName(name);
        if (role == null) {
            role = new RoleDAO(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}