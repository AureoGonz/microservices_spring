package com.triceracode.auth.security.filter;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.triceracode.core.entity.User;
import com.triceracode.core.property.JwtConfiguration;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import static com.triceracode.auth.security.utils.Constants.*;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JwtConfiguration jwtConfiguration;

	@Override
	@SneakyThrows
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		User userCredentials = new ObjectMapper().readValue(request.getInputStream(), User.class);
		if (null == userCredentials)
			throw new UsernameNotFoundException("Unable to retrive user or password");
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				userCredentials.getUsername(), userCredentials.getPassword()/* , userCredentials.getRoles() */);
		usernamePasswordAuthenticationToken.setDetails(userCredentials);
		return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
//		return authenticationManager.authenticate(
//				new UsernamePasswordAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword()));
	}

	@Override
	@SneakyThrows
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) {
		SignedJWT signedJWT = createSignedJWT(authResult);
		
		String encryptedToken = encryptedToken(signedJWT);
		
		response.addHeader("Access-Control-Expose-Headers", "XSRF-TOKEN, "+jwtConfiguration.getHeader().getName());
		response.addHeader(jwtConfiguration.getHeader().getName(), jwtConfiguration.getHeader().getPrefix()+ encryptedToken);
		
		response.setContentType("application/json");
		response.getWriter().append("{\""+TOKEN_BEARER_PREFIX+"\":\""+encryptedToken+"\"}");
	}

	@SneakyThrows
	private SignedJWT createSignedJWT(Authentication auth) {
		User user = (User) auth.getPrincipal();

		JWTClaimsSet jwtClaimsSet = createJWTClaimSet(auth, user);

		KeyPair rsaKeys = generateKeyPair();

		JWK jwk = new RSAKey.Builder((RSAPublicKey) rsaKeys.getPublic()).keyID(UUID.randomUUID().toString()).build();

		SignedJWT signedJWT = new SignedJWT(
				new JWSHeader.Builder(JWSAlgorithm.RS256).jwk(jwk).type(JOSEObjectType.JWT).build(), jwtClaimsSet);

		RSASSASigner signer = new RSASSASigner(rsaKeys.getPrivate());

		signedJWT.sign(signer);

		return signedJWT;
	}

	private JWTClaimsSet createJWTClaimSet(Authentication auth, User user) {
		return new JWTClaimsSet.Builder().subject(user.getUsername())
				.claim("authorities",
						auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.issuer(ISSUER_TOKEN)
				.expirationTime(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000)).build();
	}

	@SneakyThrows
	private KeyPair generateKeyPair() {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);

		return generator.genKeyPair();
	}

	private String encryptedToken(SignedJWT signedJWT) throws JOSEException {
		DirectEncrypter directEncrypter = new DirectEncrypter(jwtConfiguration.getPrivateKey().getBytes());
		JWEObject jweObject = new JWEObject(
				new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256).contentType("JWT").build(),
				new Payload(signedJWT));
		jweObject.encrypt(directEncrypter);

		return jweObject.serialize();
	}

}
