package com.triceracode.shoppingms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.triceracode.shoppingms.entity.Invoice;
import com.triceracode.shoppingms.entity.repository.RInvoice;
import com.triceracode.shoppingms.entity.repository.RInvoiceItem;
import com.triceracode.shoppingms.service.interfaces.IServiceInvoice;

@Service
public class SInvoice implements IServiceInvoice {

	@Autowired
	private RInvoice rInvoice;

	@Autowired
	private RInvoiceItem rItem;

	@Override
	public List<Invoice> findAll() {
		return rInvoice.findAll();
	}

	@Override
	public Invoice create(Invoice invoice) {
		Invoice invoiceDB = rInvoice.findByNumber(invoice.getNumber());
		if (invoiceDB != null)
			return invoiceDB;
		invoice.setStatus("CREATED");
		return rInvoice.save(invoice);
	}

	@Override
	public Invoice update(Invoice invoice) {
		Invoice invoiceDB = get(invoice.getId());
		if (invoiceDB == null)
			return null;
		invoiceDB.setCustomer(invoice.getCustomer());
		invoiceDB.setDescription(invoice.getDescription());
		invoiceDB.setNumber(invoice.getNumber());
		invoiceDB.getItems().clear();
		invoiceDB.setItems(invoice.getItems());
		return rInvoice.save(invoiceDB);
	}

	@Override
	public Invoice delete(Long id) {
		Invoice invoiceDB = get(id);
		if (invoiceDB == null)
			return null;
		invoiceDB.setStatus("DELETED");
		return rInvoice.save(invoiceDB);
	}

	@Override
	public Invoice get(Long id) {
		return rInvoice.findById(id).orElse(null);
	}

}
