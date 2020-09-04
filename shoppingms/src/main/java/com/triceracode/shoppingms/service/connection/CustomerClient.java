package com.triceracode.shoppingms.service.connection;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.triceracode.shoppingms.model.Customer;

@RequestMapping(path = "customers")
@FeignClient(name = "customerms")
public interface CustomerClient {

	@GetMapping(path = "/{id}")
	public ResponseEntity<Customer> get(@PathVariable long id);
}
