package com.triceracode.shoppingms.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Positive;

import org.springframework.validation.annotation.Validated;

import com.triceracode.shoppingms.model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@Builder
@Validated
public class InvoiceItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Positive(message = "El stock debe ser mayor que cero")
	private Double quantity;
	private Double price;
	private Long product;

	@Transient
	private Double subTotal;
	
	@Transient
	private Product productRef;

	public Double getSubTotal() {
		if (this.price > 0 && this.quantity > 0) {
			return this.quantity * this.price;
		} else {
			return (double) 0;
		}
	}

	public InvoiceItem() {
		this.quantity = (double) 0;
		this.price = (double) 0;
	}
}
