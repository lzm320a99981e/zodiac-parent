package com.zodiac.app.gsps;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSwagger2Doc
@SpringBootApplication(scanBasePackages = {"com.zodiac.app","com.github.lzm320a99981e"})
public class ZodiacAppGspsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZodiacAppGspsApplication.class, args);
    }

}
