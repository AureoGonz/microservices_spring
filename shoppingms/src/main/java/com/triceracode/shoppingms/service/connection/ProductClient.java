package com.triceracode.shoppingms.service.connection;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.triceracode.shoppingms.model.Product;

@RequestMapping(path = "products")
@FeignClient(name = "productms")
public interface ProductClient {

	@GetMapping(path = "/{id}")
	public ResponseEntity<Product> get(@PathVariable Long id);

	@PutMapping(path = "/{id}/stock")
	public ResponseEntity<Product> updateStock(@PathVariable Long id,
			@RequestParam(name = "quantity", required = true) Double quantity);
}
