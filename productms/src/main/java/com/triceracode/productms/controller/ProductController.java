package com.triceracode.productms.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.triceracode.productms.entity.Category;
import com.triceracode.productms.entity.Product;
import com.triceracode.productms.exception.ErrorDetails;
import com.triceracode.productms.service.interfaces.IServiceProduct;

@RestController
@RequestMapping(path = "products")
public class ProductController {

	@Autowired
	private IServiceProduct sProduct;

	@GetMapping
	public ResponseEntity<List<Product>> listAll(@RequestParam(name = "category", required = false) Long id) {
		List<Product> products = null == id ? sProduct.listAll()
				: sProduct.findByCategory(Category.builder().id(id).build());
		if (products.isEmpty())
			return null == id ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
		return ResponseEntity.ok(products);
	}

	@GetMapping(path = "/byCategory/{id}")
	public ResponseEntity<List<Product>> listAllByCategory(@PathVariable Long id) {
		List<Product> products = sProduct.findByCategory(Category.builder().id(id).build());
		return products.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(products);
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<Product> get(@PathVariable Long id) {
		Product product = sProduct.get(id);
		return null == product ? ResponseEntity.notFound().build() : ResponseEntity.ok(product);
	}

	@PostMapping
	public ResponseEntity<Product> create(@Valid @RequestBody Product product, BindingResult results) {
		if (results.hasErrors())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorDetails.formatMessage(results, "01"));
		Product created = sProduct.create(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PutMapping(path = "/{id}")
	public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
		product.setId(id);
		Product updated = sProduct.update(product);
		return null == updated ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Product> delete(@PathVariable Long id) {
		Product deleted = sProduct.delete(id);
		return null == deleted ? ResponseEntity.notFound().build() : ResponseEntity.ok(deleted);
	}

	@PutMapping(path = "/{id}/stock")
	public ResponseEntity<Product> updateStock(@PathVariable Long id,
			@RequestParam(name = "quantity", required = true) Double quantity) {
		Product product = sProduct.updateStock(id, quantity);
		return null == product ? ResponseEntity.notFound().build() : ResponseEntity.ok(product);
	}
}
