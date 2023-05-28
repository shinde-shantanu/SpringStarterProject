package com.example.demoCustomer.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/")
    public String hello(){
        return customerService.hello();
    }

    @GetMapping("/getAll")
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

}
