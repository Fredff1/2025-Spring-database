package com.repairhub.management.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import jakarta.annotation.PostConstruct;

@Configuration
public class DatabaseInitConfig {
     @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void initDatabase() {
        ResourceDatabasePopulator tablePopulator = new ResourceDatabasePopulator();
        tablePopulator.addScript(new ClassPathResource("schema.sql"));
        tablePopulator.execute(dataSource);

        ResourceDatabasePopulator triggerPopulator = new ResourceDatabasePopulator();
        triggerPopulator.setSeparator("$$");
        triggerPopulator.addScript(new ClassPathResource("schema-trigger.sql"));
        triggerPopulator.execute(dataSource);

        ResourceDatabasePopulator dataPopulator = new ResourceDatabasePopulator();
        dataPopulator.setSeparator(";");
        dataPopulator.addScript(new ClassPathResource("data.sql"));
        dataPopulator.execute(dataSource);
    }
}
