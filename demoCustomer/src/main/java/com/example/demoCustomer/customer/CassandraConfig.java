package com.example.demoCustomer.customer;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Override
    protected String getKeyspaceName() {
        return "customerDB";
    }

    @Override
    protected String getContactPoints() {
        // Return the comma-separated list of Cassandra contact points
        return "localhost";
    }

    @Override
    protected int getPort() {
        // Return the Cassandra port
        return 9042;
    }

    @Override
    public SchemaAction getSchemaAction() {
        // Return the desired schema action
        return SchemaAction.RECREATE_DROP_UNUSED;
    }

}
