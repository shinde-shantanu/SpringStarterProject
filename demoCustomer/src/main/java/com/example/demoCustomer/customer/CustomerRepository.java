package com.example.demoCustomer.customer;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends CassandraRepository<Customer, String> {

    @Query("SELECT COUNT(*) FROM customer WHERE billingAccountNumber = ?0")
    Optional<Long> findByBillingAccountNumber(String billingAccountNumber);

}
