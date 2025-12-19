package com.fitfounder.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FitFounderApplication {
    public static void main(String[] args) {
        SpringApplication.run(FitFounderApplication.class, args);
    }
}
