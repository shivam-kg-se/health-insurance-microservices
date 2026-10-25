package com.ish.ads;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class SsaAdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsaAdminServerApplication.class, args);
    }

}
