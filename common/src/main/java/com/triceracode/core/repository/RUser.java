package com.triceracode.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.triceracode.core.entity.User;

@Repository
public interface RUser extends JpaRepository<User, Long> {
	public User findByUsername(String username);
}
