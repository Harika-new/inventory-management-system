package com.harika.inventorymanagementsystem.service;

import com.harika.inventorymanagementsystem.entity.Customer;
import com.harika.inventorymanagementsystem.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    // Constructor injection:
    // Spring creates CustomerRepository and injects it here.
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Save Customer
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // Get all Customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Get Customer by ID
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    // Delete Customer by ID
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}