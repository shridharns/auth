package com.shridhar.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shridhar.auth.model.User;
import com.shridhar.auth.service.UserService;

public class UserServiceTest extends BaseTest {

   @Autowired
  private UserService userService;

    @Test
    public void testUserService() {
        
    	assertNotNull("UserService is null", userService);
        
        User user = userService.getPersonById(1);
        
        assertEquals("User Id doesn't match", "1", user.getId());
    }
    
    @Test
    public void testUserList() {
    	List<User> users = userService.getAllUsers();
    	assertNotNull("User list is null", users);
    	
    	assertEquals("User list doesn't match", 3, users.size());
    }

}
