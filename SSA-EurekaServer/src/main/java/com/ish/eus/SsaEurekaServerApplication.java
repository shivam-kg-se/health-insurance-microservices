package com.ish.eus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SsaEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsaEurekaServerApplication.class, args);
    }

}
