package com.shridhar.auth.service;

import static com.shridhar.auth.constants.Constants.FAMILY_NAME;
import static com.shridhar.auth.constants.Constants.GIVEN_NAME;
import static com.shridhar.auth.constants.Constants.IDP;
import static com.shridhar.auth.constants.Constants.ID_TOKEN;
import static com.shridhar.auth.constants.Constants.REQUIRED_CLAIMS;
import static com.shridhar.auth.constants.Constants.ROLE;
import static com.shridhar.auth.constants.Constants.SCOPE;
import static com.shridhar.auth.constants.Constants.EMAIL;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.shridhar.auth.dao.CertificatesDAO;
import com.shridhar.auth.model.LoginResponse;
import com.shridhar.auth.model.User;
import com.shridhar.auth.repository.CertificateRepository;

@Service
public class TokenService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

	private static ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<SecurityContext>();

	private static final JWSAlgorithm expectedJWSAlg = JWSAlgorithm.RS256;

	private static final SecurityContext ctx = null;

	private CertificateRepository certificateRepository;

	// 1 hour, as the default.
	public static final long TOKEN_VALIDITY_SECONDS = 1 * 60 * 60;
	
	public static final long CERT_VALIDITY_DAYS = 1;

	private JWKSource<SecurityContext> immutableJWKSet;

	private RSAKey rsaJWK;
	
	public TokenService(@Autowired CertificateRepository certificateRepository) {
		this.certificateRepository = certificateRepository;
	}

	@PostConstruct
	public void populateRSAKeys() {
		
		try {
			// Token validity time.
			ZonedDateTime tokenValidTime = ZonedDateTime.now().plusDays(CERT_VALIDITY_DAYS);
			String uuid = UUID.randomUUID().toString();

			this.rsaJWK = new RSAKeyGenerator(2048).keyID(uuid).generate();

			JWKSet jwkSet = new JWKSet(rsaJWK);

			CertificatesDAO certificatesDAO = new CertificatesDAO();
			certificatesDAO.setId(UUID.randomUUID().toString());
			certificatesDAO.setPrivateKey(new String(Base64.getEncoder().encode(rsaJWK.toPrivateKey().getEncoded())));
			certificatesDAO.setPublicKey(new String(Base64.getEncoder().encode(rsaJWK.toPublicKey().getEncoded())));
			certificatesDAO.setValidUntil(Date.from(tokenValidTime.toInstant()));

			certificateRepository.save(certificatesDAO);

			immutableJWKSet = new ImmutableJWKSet<SecurityContext>(jwkSet);
		} catch (JOSEException e) {
			LOGGER.error("Error generating RSA key pair", e);
		}
	}

	public LoginResponse getToken(User user, ScopeType scopeType) {
		return this.getToken(user, scopeType, null);
	}

	public LoginResponse getToken(User user, ScopeType scopeType, String clientId) {
		return this.getToken(user, scopeType, clientId, null);
	}

	public LoginResponse getToken(User user, ScopeType scopeType, String clientId, Long ttl) {

		LoginResponse loginResponse = null;
		
		// Create RSA-signer with the private key
		try {
			JWSSigner signer = new RSASSASigner(rsaJWK);
			SignedJWT signedJWT = new SignedJWT(
					new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJWK.getKeyID()).build(),
					buildClaimSet(user, scopeType, clientId, ttl));

			// Compute the RSA signature
			signedJWT.sign(signer);
			loginResponse = LoginResponse.builder().expiresIn(new Date().getTime())
					.idToken(signedJWT.serialize()).scope(scopeType.getName()).tokenType(ID_TOKEN).build();
		} catch (JOSEException e) {
			LOGGER.error("Error generating RSA key pair", e);
		}

		return loginResponse;

	}

	public JWTClaimsSet validateTokenAndGetClaimset(String token) {

		JWTClaimsSet claimsSet = null;

		try {

			JWKSource<SecurityContext> keySource = getJwkSource();

			JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(expectedJWSAlg, keySource);

			jwtProcessor.setJWSKeySelector(keySelector);

			// Validate the token for appropriate claims.
			jwtProcessor.setJWTClaimsSetVerifier(new DefaultJWTClaimsVerifier<SecurityContext>(
					new JWTClaimsSet.Builder().issuer(IDP).build(), new HashSet<>(Arrays.asList(REQUIRED_CLAIMS))));

			SignedJWT signedJWT = SignedJWT.parse(token);

			claimsSet = jwtProcessor.process(signedJWT, ctx);
		} catch (ParseException | BadJOSEException | JOSEException e) {
			LOGGER.error("Token couldn't be validated", e);
		}

		return claimsSet;
	}

	public JWKSource<SecurityContext> getJwkSource() {
		return this.immutableJWKSet;
	}

	private JWTClaimsSet buildClaimSet(User user, ScopeType scopeType, String clientId, Long ttl) {

		// Id token can't be extended infinitely.
		if (ttl != null && ttl > TOKEN_VALIDITY_SECONDS) {
			throw new IllegalArgumentException("Maximum allowed ttl for id token is: " + TOKEN_VALIDITY_SECONDS);
		}

		Long timeToLive = Optional.ofNullable(ttl).orElse(TOKEN_VALIDITY_SECONDS);

		// Token validity time.
		ZonedDateTime tokenValidTime = ZonedDateTime.now().plusSeconds(timeToLive);

		// Prepare JWT with claims set
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().subject(user.getId().toString()).issuer(IDP)
				.audience(clientId != null && !clientId.isEmpty() ? clientId : "defualt")
				.issueTime(new Date())
				.expirationTime(Date.from(tokenValidTime.toInstant()))
				.claim(EMAIL, user.getEmail())
				.claim(GIVEN_NAME, user.getFirstName())
				.claim(FAMILY_NAME, user.getLastName())
				.claim(SCOPE, scopeType.getName())
				.claim(ROLE, user.getRole())
				.build();

		return claimsSet;
	}
}
