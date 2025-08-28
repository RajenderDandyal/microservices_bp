package com.yourcompany.user.service;

import co.elastic.apm.attach.ElasticApmAttacher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;


@EnableRetry
@EnableFeignClients(basePackages = "com.yourcompany.user.service.client")
@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {

        ElasticApmAttacher.attach();
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
