package com.example.demoCustomer.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final StateConverter stateConverter;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, StateConverter stateConverter) {
        this.customerRepository = customerRepository;
        this.stateConverter = stateConverter;
    }

    public String hello(){
        return "Hello World";
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    private String getRandomNineDigitNumber() {
        Random random = new Random();
        int min = 100_000_000;
        int max = 999_999_999;
        int randomNumber = random.nextInt(max - min + 1) + min;
        return String.format("%09d", randomNumber);
    }

    public void createCustomer(Customer customer) {

        //Generating Billing Account Number
        String billingAccountNumber = getRandomNineDigitNumber();
        Optional<Long> count = customerRepository.findByBillingAccountNumber(billingAccountNumber);
        while(count.orElse(0L) != 0) {
            billingAccountNumber = getRandomNineDigitNumber();
            count = customerRepository.findByBillingAccountNumber(billingAccountNumber);
        }
        customer.setBillingAccountNumber(billingAccountNumber);

        //Checking for valid zip code
        String zip = customer.getAddress().getZip();
        if(!zip.matches("\\d{5}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Zip code");
        }

        //Conversion to State Code
        if(stateConverter.isValidState(customer.getAddress().getState())) {
            customer.getAddress().setState(stateConverter.convertToCode(customer.getAddress().getState()));
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid State");
        }

        customerRepository.save(customer);
    }
}
