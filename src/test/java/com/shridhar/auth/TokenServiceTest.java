package com.shridhar.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nimbusds.jwt.JWTClaimsSet;
import com.shridhar.auth.model.LoginResponse;
import com.shridhar.auth.model.User;
import com.shridhar.auth.service.ScopeType;
import com.shridhar.auth.service.TokenService;

import static com.shridhar.auth.constants.Constants.*;

public class TokenServiceTest extends BaseTest {

    @Autowired
    private TokenService tokenService;

    @Test
    public void testToken() {

        User user = new User();

        user.setId("1");

        LoginResponse loginResponse = tokenService.getToken(user, ScopeType.USER, null);

        assertNotNull("Login response is null", loginResponse);

        assertFalse("Token is empty", loginResponse.getIdToken().isEmpty());
    }

    @Test
    public void validateToken() {

        User user = new User();

        user.setId("1");

        LoginResponse loginResponse = tokenService.getToken(user, ScopeType.USER, null);

        JWTClaimsSet claimsSet = tokenService.validateTokenAndGetClaimset(loginResponse.getIdToken());

        assertEquals("Subject is not right","1", claimsSet.getSubject());

        assertEquals("Issuer is not right", IDP, claimsSet.getIssuer());

        assertEquals("Scope is not right", "openid profile", claimsSet.getClaim(SCOPE));
    }

}
