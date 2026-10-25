package com.ish.eds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SsaElgibilityDeterminationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsaElgibilityDeterminationServiceApplication.class, args);
    }

}
