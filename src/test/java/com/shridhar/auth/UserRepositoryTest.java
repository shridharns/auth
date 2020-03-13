package com.shridhar.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.shridhar.auth.dao.UserDAO;
import com.shridhar.auth.repository.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {
 
    @Autowired
    private UserRepository userRepository;
 
    @Test
    public void testRepositoryInstantiation() {
    	assertNotNull("User repository is null", userRepository);
    }
    
    @Test
    public void testUserDao() {
    	Optional<UserDAO> userDao = userRepository.findById(1);
    	assertNotNull("userDao is null", userDao);
    	assertEquals("UserDAO not returned", userDao.get().getClass(), UserDAO.class);
    	assertEquals("User name doesn't match", "One", userDao.get().getFirstName());
    }
 
}