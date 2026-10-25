package com.ish.bis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SsaBenifitInsuranceApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsaBenifitInsuranceApiApplication.class, args);
    }

}
