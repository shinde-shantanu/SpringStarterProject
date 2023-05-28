package com.example.demoCustomer.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public String hello(){
        return "Hello World";
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }
}
