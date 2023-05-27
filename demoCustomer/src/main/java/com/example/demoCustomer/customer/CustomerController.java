package com.example.demoCustomer.customer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/customer")
public class CustomerController {

    @GetMapping("/")
    public String hello(){
        return "Hello World";
    }

}
