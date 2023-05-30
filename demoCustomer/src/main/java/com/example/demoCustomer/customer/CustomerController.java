package com.example.demoCustomer.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/create")
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer,
                                         @RequestHeader("ConversationId") String conversationId) {
        customer.setConversationId(conversationId);
        return customerService.createCustomer(customer);
    }

    @GetMapping("/get/{billingAccountNumber}")
    public Customer getCustomer(@PathVariable String billingAccountNumber) {
        return customerService.getCustomer(billingAccountNumber);
    }

}
