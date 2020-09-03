package com.triceracode.productms.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.triceracode.productms.entity.Category;
import com.triceracode.productms.entity.Product;
import com.triceracode.productms.entity.repository.RProduct;
import com.triceracode.productms.service.interfaces.IServiceProduct;

@Service
public class SProduct implements IServiceProduct {

	@Autowired
	private RProduct rProduct;

	@Override
	public List<Product> listAll() {
		return rProduct.findAll();
	}

	@Override
	public Product get(Long id) {
		return rProduct.findById(id).orElse(null);
	}

	@Override
	public Product create(Product product) {
		product.setStatus("CREATED");
		product.setCreatedAt(new Date());
		return rProduct.save(product);
	}

	@Override
	public Product update(Product product) {
		Product productDB = get(product.getId());
		if (null == productDB)
			return null;
		productDB.setName(product.getName());
		productDB.setDescription(product.getDescription());
		productDB.setPrice(product.getPrice());
		productDB.setCategory(product.getCategory());
		return rProduct.save(productDB);
	}

	@Override
	public Product delete(Long id) {
		Product productDB = get(id);
		if (null == productDB)
			return null;
		productDB.setStatus("DELETED");
		return rProduct.save(productDB);
	}

	@Override
	public List<Product> findByCategory(Category category) {
		return rProduct.findByCategory(category);
	}

	@Override
	public Product updateStock(Long id, Double quantity) {
		Product productDB = get(id);
		if (null == productDB)
			return null;
		Double stock = productDB.getStock() + quantity;
		productDB.setStock(stock);
		return rProduct.save(productDB);
	}

}
