package com.triceracode.shoppingms.service.connection;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.triceracode.shoppingms.model.Customer;

@FeignClient(name = "customerms", fallback = CustomerHystrixFallbackFactory.class)
public interface CustomerClient {

	@GetMapping(path = "/customers/{id}")
	public ResponseEntity<Customer> get(@PathVariable(value="id") long id);
}
