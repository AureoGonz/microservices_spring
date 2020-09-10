package com.triceracode.gateway.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.netflix.zuul.context.RequestContext;
import com.nimbusds.jwt.SignedJWT;
import com.triceracode.core.property.JwtConfiguration;
import com.triceracode.token.converter.TokenConverter;
import com.triceracode.token.security.filter.JwtAuthorizationFilter;
import com.triceracode.token.utils.SecurityContextUtil;

import lombok.SneakyThrows;

public class GatewayJwtAuthorizationFilter extends JwtAuthorizationFilter{

	public GatewayJwtAuthorizationFilter(JwtConfiguration jwtConfiguration, TokenConverter tokenConverter) {
		super(jwtConfiguration, tokenConverter);
	}
	
	@Override
	@SneakyThrows
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader(jwtConfiguration.getHeader().getName());
		
		if(null==header || !header.startsWith(jwtConfiguration.getHeader().getPrefix())) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String token = header.replace(jwtConfiguration.getHeader().getPrefix(),"").trim();
		
		String signedToken = tokenConverter.decryptToken(token);
		tokenConverter.validateTokenSignature(signedToken);
		
		SecurityContextUtil.setSecurityContext(SignedJWT.parse(signedToken));
		
		if (jwtConfiguration.getType().equalsIgnoreCase("signed"))
            RequestContext.getCurrentContext().addZuulRequestHeader("Authorization", jwtConfiguration.getHeader().getPrefix() + signedToken);

        filterChain.doFilter(request, response);
	}
}
