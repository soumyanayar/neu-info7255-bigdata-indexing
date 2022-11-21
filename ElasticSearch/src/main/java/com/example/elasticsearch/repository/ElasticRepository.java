package com.example.elasticsearch.repository;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class ElasticRepository {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private RestHighLevelClient restHighLevelClient;

    public ElasticRepository(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    public void index(String id, String document) {
        IndexRequest request = new IndexRequest("plan", "_doc", id);
        request.source(document, XContentType.JSON);
        try {
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void delete(String id) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest("plan", "_doc", id);
            restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
