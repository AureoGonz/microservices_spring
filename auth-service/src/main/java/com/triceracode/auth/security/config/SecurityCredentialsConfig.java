package com.triceracode.auth.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.triceracode.auth.security.filter.JwtAuthenticationFilter;
import com.triceracode.core.property.JwtConfiguration;
import com.triceracode.token.creator.TokenCreator;
import com.triceracode.token.security.config.SecurityTokenConfig;

@EnableWebSecurity
public class SecurityCredentialsConfig extends SecurityTokenConfig {

	private final UserDetailsService userDetailsService;
	private final TokenCreator tokenCreator;

	public SecurityCredentialsConfig(JwtConfiguration jwtConfiguration, UserDetailsService userDetailsService,
			TokenCreator tokenCreator) {
		super(jwtConfiguration);
		this.userDetailsService = userDetailsService;
		this.tokenCreator = tokenCreator;
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtConfiguration, tokenCreator));
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
