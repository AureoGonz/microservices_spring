package com.triceracode.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.triceracode.core.property.JwtConfiguration;

@SpringBootApplication
@EnableConfigurationProperties(value= JwtConfiguration.class)
@EntityScan({"com.triceracode.core.entity"})
@EnableJpaRepositories({"com.triceracode.core.repository"})
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
