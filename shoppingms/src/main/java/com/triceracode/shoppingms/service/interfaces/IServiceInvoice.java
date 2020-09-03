package com.triceracode.shoppingms.service.interfaces;

import java.util.List;

import com.triceracode.shoppingms.entity.Invoice;

public interface IServiceInvoice {

	public List<Invoice> findAll();

    public Invoice create(Invoice invoice);
    public Invoice update(Invoice invoice);
    public Invoice delete(Long id);

    public Invoice get(Long id);
}
