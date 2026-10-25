package com.ish.cas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SsaCorrespondenceApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsaCorrespondenceApiApplication.class, args);
    }

}
