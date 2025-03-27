package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.conf com.db.dao com.db.entity")
public class WebPracApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebPracApplication.class, args);
    }
}
