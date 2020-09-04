package com.triceracode.shoppingms.service.connection;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.triceracode.shoppingms.model.Customer;

@Component
public class CustomerHystrixFallbackFactory implements CustomerClient {

	@Override
	public ResponseEntity<Customer> get(long id) {
		Customer customer = Customer.builder()
                .firstName("none")
                .lastName("none")
                .email("none")
                .photo("none").build();
        return ResponseEntity.ok(customer);
	}

}
