package com.example.devcoursed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {
        "com.example.devcoursed.domain.orders.orders.repository",
        "com.example.devcoursed.domain.member.member.repository",
        "com.example.devcoursed.domain.product.product.repository"})
public class DevcoursedApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevcoursedApplication.class, args);
    }

}
