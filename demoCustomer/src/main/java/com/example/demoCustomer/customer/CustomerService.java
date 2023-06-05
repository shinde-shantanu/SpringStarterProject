package com.example.demoCustomer.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private LoggingService loggingService;

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

    public ResponseEntity<String> createCustomer(Customer customer) {

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
            loggingService.makeErrorLog("Invalid Zip code: " + zip);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Zip code");
        }

        //Conversion to State Code
        if(stateConverter.isValidState(customer.getAddress().getState())) {
            customer.getAddress().setState(stateConverter.convertToCode(customer.getAddress().getState()));
        }
        else {
            loggingService.makeErrorLog("Invalid State: " + customer.getAddress().getState());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid State");
        }

        //Checking for valid phone number
        String phoneNo = customer.getPhoneNo();
        if(!phoneNo.matches("\\d{10}")) {
            loggingService.makeErrorLog("Invalid phoneNo.: " + customer.getPhoneNo());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Phone Number");
        }

        customerRepository.save(customer);

        loggingService.makeInfoLog("Customer: " + customer + " created successfully");
        return ResponseEntity.ok("Customer created successfully");
    }

    public Customer getCustomer(String billingAccountNumber) {
        Optional<Customer> customer = customerRepository.findById(billingAccountNumber);
        loggingService.makeInfoLog("Customer: " + billingAccountNumber + " found");
        return customer.get();
    }

    public ResponseEntity<String> deleteCustomer(String billingAccountNumber) {
        boolean exists = customerRepository.existsById(billingAccountNumber);
        if(!exists) {
            loggingService.makeInfoLog("Customer: " + billingAccountNumber + " not found");
            return ResponseEntity.notFound().build();
        }
        customerRepository.deleteById(billingAccountNumber);
        loggingService.makeInfoLog("Customer: " + billingAccountNumber + "deleted successfully");
        return ResponseEntity.ok("Customer deleted successfully");
    }

    public ResponseEntity<String> updateCustomer(String billingAccountNumber, Customer updateCustomer) {  //Need to maintain customer history

        Optional<Customer> optionalCustomer = customerRepository.findById(billingAccountNumber);
        if(optionalCustomer.isPresent()){
            Customer customer = optionalCustomer.get();

            if(updateCustomer.getFirstName() != null) {
                customer.setFirstName(updateCustomer.getFirstName());
            }
            if(updateCustomer.getLastName() != null) {
                customer.setLastName(updateCustomer.getLastName());
            }
            if(updateCustomer.getAddress().getLine1() != null) {
                customer.getAddress().setLine1(updateCustomer.getAddress().getLine1());
            }
            if(updateCustomer.getAddress().getLine2() != null) {
                customer.getAddress().setLine2(updateCustomer.getAddress().getLine2());
            }
            if(updateCustomer.getAddress().getCity() != null) {
                customer.getAddress().setCity(updateCustomer.getAddress().getCity());
            }
            if(updateCustomer.getAddress().getZip() != null) {
                if(!updateCustomer.getAddress().getZip().matches("\\d{5}")){
                    loggingService.makeErrorLog("Invalid Zip code: " + updateCustomer.getAddress().getZip());
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Zip code");
                }
                customer.getAddress().setZip(updateCustomer.getAddress().getZip());
            }
            if(updateCustomer.getAddress().getState() != null) {
                if(updateCustomer.getAddress().getState().length() != 2){
                    if(stateConverter.isValidState(updateCustomer.getAddress().getState())) {
                        updateCustomer.getAddress().setState(stateConverter.convertToCode(updateCustomer.getAddress().getState()));
                    }
                    else {
                        loggingService.makeErrorLog("Invalid State: " + customer.getAddress().getState());
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid State");
                    }
                }
                customer.getAddress().setState(updateCustomer.getAddress().getState());
            }
            if(updateCustomer.getEmailId() != null) {
                customer.setEmailId(updateCustomer.getEmailId());
            }
            if(updateCustomer.getPhoneNo() != null) {
                if(!updateCustomer.getPhoneNo().matches("\\d{10}")){
                    loggingService.makeErrorLog("Invalid phoneNo.: " + updateCustomer.getPhoneNo());
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Phone Number");
                }
                customer.setPhoneNo(updateCustomer.getPhoneNo());
            }

            customerRepository.save(customer);

            loggingService.makeInfoLog("Customer: " + customer + " updated successfully");
            return ResponseEntity.ok("Customer updated successfully");
        }
        else {
            loggingService.makeInfoLog("Customer: " + billingAccountNumber + " not found");
            return ResponseEntity.notFound().build();
        }

    }
}
