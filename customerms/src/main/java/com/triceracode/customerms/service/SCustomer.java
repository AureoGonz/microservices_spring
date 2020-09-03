package com.triceracode.customerms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.triceracode.customerms.entity.Customer;
import com.triceracode.customerms.entity.Region;
import com.triceracode.customerms.entity.repository.RCustomer;
import com.triceracode.customerms.service.interfaces.IServiceCustomer;

@Service
public class SCustomer implements IServiceCustomer {

	@Autowired
	private RCustomer rCustomer;

	@Override
	public List<Customer> findAll() {
		return rCustomer.findAll();
	}

	@Override
	public List<Customer> findByRegion(Region region) {
		return rCustomer.findByRegion(region);
	}

	@Override
	public Customer create(Customer customer) {
		Customer customerDB = rCustomer.findByEmail(customer.getEmail());
		if (customerDB != null)
			return customerDB;
		customer.setStatus("CREATED");
		customerDB = rCustomer.save(customer);
		return customerDB;
	}

	@Override
	public Customer update(Customer customer) {
		Customer customerDB = get(customer.getId());
        if (customerDB == null)
            return  null;
        customerDB.setFirstName(customer.getFirstName());
        customerDB.setLastName(customer.getLastName());
        customerDB.setEmail(customer.getEmail());
        customerDB.setPhoto(customer.getPhoto());

        return rCustomer.save(customerDB);
	}

	@Override
	public Customer delete(Long id) {
		Customer customerDB = get(id);
        if (customerDB ==null)
            return  null;
        customerDB.setStatus("DELETED");
        return rCustomer.save(customerDB);
	}

	@Override
	public Customer get(Long id) {
		return rCustomer.findById(id).orElse(null);
	}

}
