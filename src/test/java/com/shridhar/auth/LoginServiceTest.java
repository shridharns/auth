package com.shridhar.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.shridhar.auth.dao.UserDAO;
import com.shridhar.auth.model.LoginResponse;
import com.shridhar.auth.model.LoginUser;
import com.shridhar.auth.repository.LoginRepository;
import com.shridhar.auth.service.LoginService;
import com.shridhar.auth.service.ScopeType;

@RunWith(SpringRunner.class)
public class LoginServiceTest {

	// Test class using Mock beans
	@TestConfiguration
	static class LoginServiceConfig {

		@Bean
		public LoginService loginService() {
			return new LoginService(null, null, null, null);
		}
	}

	@MockBean
	private LoginService loginService;

	@MockBean
	private LoginRepository loginRepository;

	@Before
	public void before() {
		UserDAO userDao = new UserDAO();
		userDao.setFirstName("testFirst");
		userDao.setLastName("testLast");
		userDao.setEmail("test@test.com");
		Mockito.when(loginRepository.login("test@test.com")).thenReturn(userDao);

		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setExpiresIn(new Date().getTime());
		loginResponse.setIdToken("id-token");
		loginResponse.setScope("openid profile");
		loginResponse.setTokenType(ScopeType.USER.getName());

		try {
			Mockito.when(loginService.login(Mockito.any())).thenReturn(loginResponse);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testLoginService() {
		assertNotNull("Login service is null", loginService);
	}

	@Test
	public void testLoginRepository() {

		assertNotNull(loginRepository.login("test@test.com"));
		assertEquals("Email doesn't match", "test@test.com", loginRepository.login("test@test.com").getEmail());
		assertEquals("First name doesn't match", "testFirst", loginRepository.login("test@test.com").getFirstName());
		assertEquals("Last name doesn't match", "testLast", loginRepository.login("test@test.com").getLastName());

	}

	@Test
	public void testLoginThroughService() {
		
		LoginUser user = new LoginUser();
		user.setClientId("c1234");

		try {
			LoginResponse loginResponse = loginService.login(user);
			assertNotNull("Login response is null", loginResponse);
			assertEquals("Login id token doesn't match", "id-token", loginResponse.getIdToken());
			assertNotNull("ExpiresIn is null", loginResponse.getExpiresIn());
			assertEquals("Last name doesn't match", "openid profile", loginResponse.getScope());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

}
