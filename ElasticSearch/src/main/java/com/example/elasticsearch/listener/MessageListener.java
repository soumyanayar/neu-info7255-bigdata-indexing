package com.example.elasticsearch.listener;


import com.example.elasticsearch.configuration.MQConfig;
import com.example.elasticsearch.model.ConsumerMessage;
import com.example.elasticsearch.repository.CustomerRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class MessageListener {

    @Autowired
    private CustomerRepository repository;
    @RabbitListener(queues = MQConfig.QUEUE)
    public void listener(ConsumerMessage consumerMessage) {
        System.out.println("Action: " + consumerMessage.getAction() + " Key: " + consumerMessage.getKey());
        if(consumerMessage.getAction().equals("create")) {
            repository.index(consumerMessage.getKey(), consumerMessage.getPayload());
        } else if(consumerMessage.getAction().equals("delete")) {
            repository.delete(consumerMessage.getKey());
        }
    }
}

