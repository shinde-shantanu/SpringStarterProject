package com.example.demoCustomer.customer;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
public class CassandraConfig {

    @Bean
    public CqlSession cassandraSession() {
        return CqlSession.builder()
                //.addContactPoint(new InetSocketAddress("127.0.0.1", 9024))
                //.withLocalDatacenter("datacenter1")
                .build();
    }

}
