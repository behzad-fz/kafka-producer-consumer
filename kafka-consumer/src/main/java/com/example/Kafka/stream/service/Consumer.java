package com.example.Kafka.stream.service;

import org.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Consumer {
    @KafkaListener(topics = "${kafka.topic.name}")
    public void processMessage(String content) {
        System.out.printf("New message received! =>  " + content);
        System.out.print("\n");

        JSONObject json = new JSONObject(content);
        JSONObject requestBody = new JSONObject(json.getString("requestBody"));
        System.out.printf(requestBody.getString("message"));

        try {
            String message = "["+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +"]"
                    +"=> User with IP '"+json.getString("userIp")+"' pinged and said: "+requestBody.getString("message")
                    +"! the answer is PONG!";

            FileWriter myWriter = new FileWriter("ping.log", true);
            myWriter.write(message);
            myWriter.write("\n");
            myWriter.close();

        } catch (IOException e) {
            System.out.print("OPS!!");
            System.out.printf(e.getMessage());
        }
    }
}

