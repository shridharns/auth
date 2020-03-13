package com.shridhar.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shridhar.auth.model.User;
import com.shridhar.auth.service.UserService;

public class UserServiceTest extends BaseTest {

   @Autowired
  private UserService userService;

    @Test
    public void testUserService() {
        
    	assertNotNull(userService);
        
        User user = userService.getPersonById(1);
        
        assertEquals("1", user.getId());
    }

}
