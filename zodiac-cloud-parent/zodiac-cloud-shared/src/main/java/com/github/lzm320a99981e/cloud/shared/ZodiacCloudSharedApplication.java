package com.github.lzm320a99981e.cloud.shared;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ZodiacCloudSharedApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZodiacCloudSharedApplication.class, args);
    }

}
