package com.triceracode.auth.security.filter;


import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import com.triceracode.core.entity.User;
import com.triceracode.core.property.JwtConfiguration;
import com.triceracode.token.creator.TokenCreator;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import static com.triceracode.token.utils.Constants.*;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JwtConfiguration jwtConfiguration;
	private final TokenCreator tokenCreator;

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
		SignedJWT signedJWT = tokenCreator.createSignedJWT(authResult);
		
		String encryptedToken = tokenCreator.encryptedToken(signedJWT);
		
		response.addHeader("Access-Control-Expose-Headers", "XSRF-TOKEN, "+jwtConfiguration.getHeader().getName());
		response.addHeader(jwtConfiguration.getHeader().getName(), jwtConfiguration.getHeader().getPrefix()+ encryptedToken);
		
		response.setContentType("application/json");
		response.getWriter().append("{\""+TOKEN_BEARER_PREFIX+"\":\""+encryptedToken+"\"}");
	}

}
