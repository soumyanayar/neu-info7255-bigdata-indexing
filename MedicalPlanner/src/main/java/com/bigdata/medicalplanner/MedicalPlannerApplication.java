package com.bigdata.medicalplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
public class MedicalPlannerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedicalPlannerApplication.class, args);
    }
}
