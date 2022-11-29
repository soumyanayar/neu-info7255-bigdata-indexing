package com.example.elasticsearch.repository;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


@Repository
public class CustomerRepository {

    private RestHighLevelClient client;

    public CustomerRepository(RestHighLevelClient client) {
        this.client = client;
    }

    public void index(String id, String document) {
        IndexRequest request = new IndexRequest("payload", "action", id);
        request.source(document);
        try {
            client.index(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            LoggerFactory.getLogger(CustomerRepository.class).error(e.getMessage());

        }
    }

    public void delete(String id) {
        try {
            client.delete(null, RequestOptions.DEFAULT);
        } catch (Exception e) {
            LoggerFactory.getLogger(CustomerRepository.class).error(e.getMessage());
        }
    }
}
