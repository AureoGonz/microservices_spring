package com.triceracode.shoppingms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import com.triceracode.core.property.JwtConfiguration;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableHystrix
@EnableHystrixDashboard
@EnableConfigurationProperties(value= JwtConfiguration.class)
@ComponentScan({"com.triceracode"})
public class ShoppingmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingmsApplication.class, args);
	}

}
