package com.triceracode.productms.service.interfaces;

import java.util.List;

import com.triceracode.productms.entity.Category;
import com.triceracode.productms.entity.Product;

public interface IServiceProduct {
	
	public List<Product> listAll();
	public Product get(Long id);
	public Product create(Product product);
	public Product update(Product product);
	public Product delete(Long id);
	public List<Product> findByCategory(Category category);
	public Product updateStock(Long id, Double quantity);

}
