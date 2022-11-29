package com.example.elasticsearch;
/*
import com.example.elasticsearch.model.Customer;
import com.example.elasticsearch.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
*/

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication

public class ElasticSearchApplication {
/*
    @Autowired
    CustomerRepository repository;

    @PostMapping("/customers")
    public void saveCustomer(@RequestBody Customer customer) {
        repository.save(customer);
    }

    @GetMapping("/customers")
    public List<Customer> getCustomers() {
        return (List<Customer>) repository.findAll();
    }

    @GetMapping("/customers/{firstName}")
    public List<Customer> getCustomersByFirstName(@PathVariable String firstName) {
        return repository.findByFirstName(firstName);
    }
*/
    public static void main(String[] args) {
        SpringApplication.run(ElasticSearchApplication.class, args);
    }

}
