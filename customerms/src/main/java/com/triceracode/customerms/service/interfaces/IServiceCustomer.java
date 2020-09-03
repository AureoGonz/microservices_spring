package com.triceracode.customerms.service.interfaces;

import java.util.List;

import com.triceracode.customerms.entity.Customer;
import com.triceracode.customerms.entity.Region;

public interface IServiceCustomer {

	public List<Customer> findAll();
    public List<Customer> findByRegion(Region region);

    public Customer create(Customer customer);
    public Customer update(Customer customer);
    public Customer delete(Long id);
    public  Customer get(Long id);
}
