package com.ish.cfs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class SsaConfigServerGitApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsaConfigServerGitApplication.class, args);
    }

}
