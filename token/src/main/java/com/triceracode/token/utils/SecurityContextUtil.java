package com.triceracode.token.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.triceracode.core.entity.Role;
import com.triceracode.core.entity.User;

public class SecurityContextUtil {
	private SecurityContextUtil() {
	}

	public static void setSecurityContext(SignedJWT signedJWT) {
		try {
			JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
			String username = claims.getSubject();
			if (null == username)
				throw new JOSEException("Nombre de usario no encontrado");
			List<String> authorities = claims.getStringListClaim("authorities");
			UserDetailCustom user = new UserDetailCustom(User.builder().id(claims.getLongClaim("userId"))
					.username(username)
					.roles(authorities.stream().map(e -> new Role(null, e, "")).collect(Collectors.toSet())).build());
			UsernamePasswordAuthenticationToken auth =  new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			auth.setDetails(signedJWT.serialize());
			SecurityContextHolder.getContext().setAuthentication(auth);
		} catch (Exception e) {
			SecurityContextHolder.clearContext();
		}
	}

	private static final class UserDetailCustom extends User implements UserDetails {

		private static final long serialVersionUID = 1L;

		public UserDetailCustom(User user) {
			super(user);
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			Set<SimpleGrantedAuthority> authorities = new HashSet<>();
			this.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
			return authorities;
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}

	}
}
