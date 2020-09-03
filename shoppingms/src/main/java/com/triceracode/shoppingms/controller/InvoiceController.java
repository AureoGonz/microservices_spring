package com.triceracode.shoppingms.controller;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.triceracode.shoppingms.entity.Invoice;
import com.triceracode.shoppingms.exception.ErrorDetails;
import com.triceracode.shoppingms.service.interfaces.IServiceInvoice;

@RestController
@RequestMapping(path = "invoices")
public class InvoiceController {

	@Autowired
	private IServiceInvoice sInvoice;

	@GetMapping
	public ResponseEntity<List<Invoice>> listAll() {
		List<Invoice> invoices = sInvoice.findAll();
		return invoices.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(invoices);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Invoice> get(@PathVariable("id") long id) {
		Invoice invoice = sInvoice.get(id);
		return null == invoice ? ResponseEntity.notFound().build() : ResponseEntity.ok(invoice);
	}

	@PostMapping
	public ResponseEntity<Invoice> create(@Valid @RequestBody Invoice invoice, BindingResult result) {
		if (result.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorDetails.formatMessage(result, "01"));
		}
		Invoice created = sInvoice.create(invoice);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<?> updateInvoice(@PathVariable("id") long id, @RequestBody Invoice invoice) {
		invoice.setId(id);
		Invoice updated = sInvoice.update(invoice);
		return null == updated ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Invoice> deleteInvoice(@PathVariable("id") long id) {
		Invoice deleted = sInvoice.delete(id);
		return null == deleted ? ResponseEntity.notFound().build() : ResponseEntity.ok(deleted);
	}
}
