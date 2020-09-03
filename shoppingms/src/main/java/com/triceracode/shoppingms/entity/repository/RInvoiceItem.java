package com.triceracode.shoppingms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.triceracode.shoppingms.entity.InvoiceItem;

@Repository
public interface RInvoiceItem extends JpaRepository<InvoiceItem, Long>{

}
