package com.triceracode.auth.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.triceracode.auth.security.filter.JwtAuthenticationFilter;
import com.triceracode.core.property.JwtConfiguration;
import com.triceracode.token.converter.TokenConverter;
import com.triceracode.token.creator.TokenCreator;
import com.triceracode.token.security.config.SecurityTokenConfig;
import com.triceracode.token.security.filter.JwtAuthorizationFilter;

@EnableWebSecurity
public class SecurityCredentialsConfig extends SecurityTokenConfig {

	private final UserDetailsService userDetailsService;
	private final TokenCreator tokenCreator;
	private final TokenConverter tokenConverter;

	public SecurityCredentialsConfig(JwtConfiguration jwtConfiguration, UserDetailsService userDetailsService,
			TokenCreator tokenCreator, TokenConverter tokenConverter) {
		super(jwtConfiguration);
		this.userDetailsService = userDetailsService;
		this.tokenCreator = tokenCreator;
		this.tokenConverter = tokenConverter;
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtConfiguration, tokenCreator))
		.addFilterAfter(new JwtAuthorizationFilter(jwtConfiguration, tokenConverter), UsernamePasswordAuthenticationFilter.class);
		super.configure(http);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
