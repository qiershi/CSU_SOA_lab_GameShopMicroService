package com.csu.captchaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CaptchaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaptchaServiceApplication.class, args);
    }

}
