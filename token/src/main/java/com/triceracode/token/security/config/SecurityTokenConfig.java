package com.triceracode.token.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.cors.CorsConfiguration;

import com.triceracode.core.property.JwtConfiguration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {

	protected final JwtConfiguration jwtConfiguration;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
				.and().sessionManagement().sessionCreationPolicy(STATELESS).and().exceptionHandling()
				.authenticationEntryPoint((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
				.authorizeRequests().antMatchers(jwtConfiguration.getLoginUri()).permitAll().anyRequest()
				.authenticated();
	}

}
