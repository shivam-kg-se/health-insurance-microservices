package com.ish.aaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SsaAdminApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsaAdminApiApplication.class, args);
    }

}
