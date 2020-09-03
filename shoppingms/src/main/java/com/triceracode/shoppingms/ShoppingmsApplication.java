package com.triceracode.shoppingms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ShoppingmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingmsApplication.class, args);
	}

}
