package com.example.Kafka.stream.service;

import org.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @KafkaListener(topics = "${kafka.topic.name}")
    public void processMessage(String content) {
        System.out.printf("New message received! =>  " + content);
        System.out.print("\n");

        JSONObject json = new JSONObject(content);
        JSONObject requestBody = new JSONObject(json.getString("requestBody"));
        System.out.printf(requestBody.getString("message"));
    }

}

