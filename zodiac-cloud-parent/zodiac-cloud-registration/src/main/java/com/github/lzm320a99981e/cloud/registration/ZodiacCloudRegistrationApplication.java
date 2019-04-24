package com.github.lzm320a99981e.cloud.registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ZodiacCloudRegistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZodiacCloudRegistrationApplication.class, args);
    }

}
