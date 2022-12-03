package com.example.elasticsearch.service;

import org.json.JSONObject;

public interface IndexingService {
    void postDocument(JSONObject document);
    void deleteDocument(JSONObject document);
}
