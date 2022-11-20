package com.example.elasticsearch;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
@Component
public class MessageListener {

    @RabbitListener(queues = MQConfig.QUEUE)
    public void listener(ConsumerMessage consumerMessage) {
        System.out.println("Action: " + consumerMessage.getAction() + " Key: " + consumerMessage.getKey());
    }
}

