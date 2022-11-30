package com.example.elasticsearch.repository;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


@Repository
public class ConsumerRepository {

    private RestHighLevelClient client;

    public ConsumerRepository(RestHighLevelClient client) {
        this.client = client;
    }

    public void index(String id, String document) {
        IndexRequest request = new IndexRequest("payload", "action", id);
        request.source(document, XContentType.JSON);
        try {
            client.index(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            LoggerFactory.getLogger(ConsumerRepository.class).error(e.getMessage());
        }
    }

    public void delete(String id) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest("payload", "action", id);
            client.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            LoggerFactory.getLogger(ConsumerRepository.class).error(e.getMessage());
        }
    }
}
