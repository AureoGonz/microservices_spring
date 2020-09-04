package com.triceracode.shoppingms.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.triceracode.shoppingms.entity.Invoice;
import com.triceracode.shoppingms.entity.InvoiceItem;
import com.triceracode.shoppingms.entity.repository.RInvoice;
import com.triceracode.shoppingms.entity.repository.RInvoiceItem;
import com.triceracode.shoppingms.model.Customer;
import com.triceracode.shoppingms.model.Product;
import com.triceracode.shoppingms.service.connection.CustomerClient;
import com.triceracode.shoppingms.service.connection.ProductClient;
import com.triceracode.shoppingms.service.interfaces.IServiceInvoice;

@Service
public class SInvoice implements IServiceInvoice {

	@Autowired
	private RInvoice rInvoice;

	@Autowired
	private RInvoiceItem rItem;

	@Autowired
	private CustomerClient customerClient;

	@Autowired
	private ProductClient productClient;

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
		invoiceDB = rInvoice.save(invoice);
		invoiceDB.getItems().forEach(invoiceItem -> {
			productClient.updateStock(invoiceItem.getProduct(), invoiceItem.getQuantity() * -1);
		});
		return invoiceDB;
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
		Invoice invoice = rInvoice.findById(id).orElse(null);
		if (null != invoice) {
			Customer customer = customerClient.get(invoice.getCustomer()).getBody();
			invoice.setCustomerRef(customer);
			List<InvoiceItem> listItem = invoice.getItems().stream().map(invoiceItem -> {
				Product product = productClient.get(invoiceItem.getProduct()).getBody();
				invoiceItem.setProductRef(product);
				return invoiceItem;
			}).collect(Collectors.toList());
			invoice.setItems(listItem);
		}
		return invoice;
	}

}
