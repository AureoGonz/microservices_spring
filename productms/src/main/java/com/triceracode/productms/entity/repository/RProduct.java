package com.triceracode.productms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.triceracode.productms.entity.Category;
import com.triceracode.productms.entity.Product;

@Repository
public interface RProduct extends JpaRepository<Product, Long>{

	public List<Product> findByCategory(Category category);
}
