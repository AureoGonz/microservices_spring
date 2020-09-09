package com.triceracode.auth.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.triceracode.core.entity.User;
import com.triceracode.core.repository.RUser;

@Service
public class SUser implements UserDetailsService{

	@Autowired
	private RUser rUser;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = rUser.findByUsername(username);
		if(null==user)
			throw new UsernameNotFoundException(String.format("Application User '%s' not found", username));
		return new UserDetailCustom(user);
	}

	private static final class UserDetailCustom extends User implements UserDetails{

		private static final long serialVersionUID = 1L;

		public UserDetailCustom(User user) {
			super(user);
		}
		
		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			Set<SimpleGrantedAuthority> authorities = new HashSet<>();
			this.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())));
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
