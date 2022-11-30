package com.example.elasticsearch.listener;


import com.example.elasticsearch.configuration.MQConfig;
import com.example.elasticsearch.model.ConsumerMessage;
import com.example.elasticsearch.repository.ConsumerRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class MessageListener {

    @Autowired
    private ConsumerRepository consumerRepository;
    @RabbitListener(queues = MQConfig.QUEUE)
    public void listener(ConsumerMessage consumerMessage) {
        System.out.println("Action: " + consumerMessage.getAction() + " Key: " + consumerMessage.getKey());
        if(consumerMessage.getAction().equals("create") ||consumerMessage.getAction().equals("Plan updated(With Patch Operation)") || consumerMessage.getAction().equals("Plan updated(With Put Operation)")) {
            consumerRepository.index(consumerMessage.getKey(), consumerMessage.getPayload());
        }
        else if(consumerMessage.getAction().equals("delete")) {
            consumerRepository.delete(consumerMessage.getKey());
        }
    }
}

