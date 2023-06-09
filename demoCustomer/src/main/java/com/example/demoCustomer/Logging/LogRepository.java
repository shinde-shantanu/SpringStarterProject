package com.example.demoCustomer.Logging;

import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface LogRepository extends CassandraRepository<Log, UUID> {
}