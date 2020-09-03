package com.triceracode.customerms.controller;

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

import com.triceracode.customerms.entity.Customer;
import com.triceracode.customerms.entity.Region;
import com.triceracode.customerms.service.interfaces.IServiceCustomer;
import com.triceracode.customerms.exception.ErrorDetails;

@RestController
@RequestMapping(path = "customers")
public class CustomerController {

	@Autowired
	private IServiceCustomer sCustomer;

	@GetMapping
	public ResponseEntity<List<Customer>> listAll(@RequestParam(name = "region", required = false) Long id) {
		List<Customer> customers = null == id ? sCustomer.findAll()
				: sCustomer.findByRegion(Region.builder().id(id).build());
		if (customers.isEmpty())
			return null == id ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
		return ResponseEntity.ok(customers);
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<Customer> get(@PathVariable long id) {
		Customer customer = sCustomer.get(id);
		return null == customer ? ResponseEntity.notFound().build() : ResponseEntity.ok(customer);
	}

	@PostMapping
	public ResponseEntity<Customer> create(@Valid @RequestBody Customer customer, BindingResult results) {
		if (results.hasErrors())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorDetails.formatMessage(results, "01"));
		Customer customerDB = sCustomer.create(customer);
		return ResponseEntity.status(HttpStatus.CREATED).body(customerDB);
	}

	@PutMapping(path = "/{id}")
	public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Customer customer) {
		customer.setId(id);
		Customer updated = sCustomer.update(customer);
		return null == updated ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Customer> delete(@PathVariable("id") long id) {
		Customer deleted = sCustomer.delete(id);
		return null == deleted ? ResponseEntity.notFound().build() : ResponseEntity.ok(deleted);
	}
}
