package com.example.elasticsearch.listener;

import com.example.elasticsearch.configuration.MQConfig;
import com.example.elasticsearch.model.ConsumerMessage;
import com.example.elasticsearch.service.IndexingService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.json.JSONArray;
import org.json.JSONObject;

@Component
public class MessageListener {
    private final IndexingService indexingService;
    @Autowired
    public MessageListener(IndexingService indexingService) {
        this.indexingService = indexingService;
    }

    @RabbitListener(queues = MQConfig.QUEUE)
    public void listener(ConsumerMessage consumerMessage) {
        System.out.println("Action: " + consumerMessage.getAction() + " Key: " + consumerMessage.getKey());
        switch (consumerMessage.getAction()) {
            case "SAVE":
                JSONObject jsonObject = new JSONObject(consumerMessage.getPayload());
                indexingService.postDocument(jsonObject);
                break;
            case "DELETE":
                indexingService.deleteDocument(new JSONObject(consumerMessage.getPayload()));
                break;
        }
    }

    public void extractKeyFromMessage(String key){
        String actualKey = key.substring(5, key.length() - 1);
    }
}

