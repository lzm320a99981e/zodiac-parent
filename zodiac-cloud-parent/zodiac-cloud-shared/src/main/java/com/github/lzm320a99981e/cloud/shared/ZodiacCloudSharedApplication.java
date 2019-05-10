package com.github.lzm320a99981e.cloud.shared;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableSwagger2Doc
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.github.lzm320a99981e.cloud")
public class ZodiacCloudSharedApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZodiacCloudSharedApplication.class, args);
    }

}
