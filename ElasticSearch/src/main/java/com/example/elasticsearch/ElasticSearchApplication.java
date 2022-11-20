package com.example.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@ContextConfiguration("classpath:META-INF/spring/rabbitmq-producer.xml")
public class ElasticSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticSearchApplication.class, args);
    }

}
