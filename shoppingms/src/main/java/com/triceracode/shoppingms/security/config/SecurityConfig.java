package com.triceracode.shoppingms.security.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.triceracode.core.property.JwtConfiguration;
import com.triceracode.token.converter.TokenConverter;
import com.triceracode.token.security.config.SecurityTokenConfig;
import com.triceracode.token.security.filter.JwtAuthorizationFilter;


@EnableWebSecurity
public class SecurityConfig extends SecurityTokenConfig{

	private final TokenConverter tokenConverter;
	
	public SecurityConfig(JwtConfiguration jwtConfiguration, TokenConverter tokenConverter) {
		super(jwtConfiguration);
		this.tokenConverter = tokenConverter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterAfter(new JwtAuthorizationFilter(jwtConfiguration, tokenConverter),UsernamePasswordAuthenticationFilter.class);
		super.configure(http);
	}
	
	

}
