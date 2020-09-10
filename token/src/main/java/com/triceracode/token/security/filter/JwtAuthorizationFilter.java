package com.triceracode.token.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nimbusds.jwt.SignedJWT;
import com.triceracode.core.property.JwtConfiguration;
import com.triceracode.token.converter.TokenConverter;
import com.triceracode.token.utils.SecurityContextUtil;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtAuthorizationFilter extends OncePerRequestFilter {
	protected final JwtConfiguration jwtConfiguration;
	protected final TokenConverter tokenConverter;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader(jwtConfiguration.getHeader().getName());
		
		if(null==header || !header.startsWith(jwtConfiguration.getHeader().getPrefix())) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String token = header.replace(jwtConfiguration.getHeader().getPrefix(),"").trim();
		
		SignedJWT signedJWT = StringUtils.equalsIgnoreCase("signed", jwtConfiguration.getType()) ? validate(token) : decryptValidating(token);
		
		SecurityContextUtil.setSecurityContext(signedJWT);
		
		filterChain.doFilter(request, response);
	}
	
	@SneakyThrows
	private SignedJWT decryptValidating(String encryptedToken) {
		String signedToken = tokenConverter.decryptToken(encryptedToken);
		tokenConverter.validateTokenSignature(signedToken);
		return SignedJWT.parse(signedToken);
	}
	
	@SneakyThrows
	private SignedJWT validate(String signedToken) {
		tokenConverter.validateTokenSignature(signedToken);
		return SignedJWT.parse(signedToken);
	}
}
