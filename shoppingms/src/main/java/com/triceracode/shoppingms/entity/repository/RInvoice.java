package com.triceracode.shoppingms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.triceracode.shoppingms.entity.Invoice;

@Repository
public interface RInvoice extends JpaRepository<Invoice, Long> {
	public List<Invoice> findByCustomer(Long customer);
    public Invoice findByNumber(String number);
}
