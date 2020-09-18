package com.triceracode.shoppingms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.triceracode.core.property.JwtConfiguration;
import com.triceracode.shoppingms.service.connection.CustomerClient;
import com.triceracode.shoppingms.service.connection.ProductClient;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
@EnableHystrixDashboard
@EnableFeignClients(clients = {CustomerClient.class, ProductClient.class})
@EntityScan({"com.triceracode.core.entity","com.triceracode.shoppingms.entity"})
@EnableJpaRepositories({"com.triceracode.core.repository","com.triceracode.shoppingms.entity.repository"})
@EnableConfigurationProperties(value= JwtConfiguration.class)
@ComponentScan("com.triceracode")
public class ShoppingmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingmsApplication.class, args);
	}

}
