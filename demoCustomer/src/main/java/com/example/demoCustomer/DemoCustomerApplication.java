package com.example.demoCustomer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoCustomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoCustomerApplication.class, args);
	}

}
