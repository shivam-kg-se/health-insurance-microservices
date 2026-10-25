package com.ish.cag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SsaCloudApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsaCloudApiGatewayApplication.class, args);
    }

}
