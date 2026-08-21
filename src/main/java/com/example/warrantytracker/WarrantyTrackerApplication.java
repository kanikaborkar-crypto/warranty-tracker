package com.example.warrantytracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WarrantyTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarrantyTrackerApplication.class, args);
    }
}