package com.sparta.spartdelivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@ImportAutoConfiguration({FeignAutoConfiguration.class})
@EnableFeignClients
@SpringBootApplication
public class SpartDeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpartDeliveryApplication.class, args);
    }

}
