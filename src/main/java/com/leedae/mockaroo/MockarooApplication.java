package com.leedae.mockaroo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class MockarooApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockarooApplication.class, args);
    }

}
