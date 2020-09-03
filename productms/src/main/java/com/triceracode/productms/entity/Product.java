package com.triceracode.productms.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message = "El nombre no debe ser vacío")
	private String name;
	private String description;
	
	@Positive(message = "El stock debe ser mayor o igual a cero")
	private Double stock;
	private Double price;
	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@NotNull(message = "La categoria debe especificarse")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="category")
	private Category category;

}
