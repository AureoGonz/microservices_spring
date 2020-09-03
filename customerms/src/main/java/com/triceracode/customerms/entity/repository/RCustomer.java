package com.triceracode.customerms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.triceracode.customerms.entity.Customer;
import com.triceracode.customerms.entity.Region;

@Repository
public interface RCustomer extends JpaRepository<Customer, Long>{
	public Customer findByEmail(String email);
    public List<Customer> findByLastName(String lastName);
    public List<Customer> findByRegion(Region region);
}
