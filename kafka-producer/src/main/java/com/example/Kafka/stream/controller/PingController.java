package com.example.Kafka.stream.controller;

import com.example.Kafka.stream.dto.MessageDto;
import com.example.Kafka.stream.service.Producer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class PingController {
    private final Producer producer;

    public PingController(Producer producer) {
        this.producer = producer;
    }

    @PostMapping("/ping")
    public ResponseEntity<String> ping(@RequestBody String requestBody, HttpServletRequest request) throws JsonProcessingException {
        JSONObject json = new JSONObject();
        json.put("requestBody", requestBody);
        json.put("userIp", request.getRemoteAddr());
        json.put("userAgent", request.getHeader("User-Agent"));
        json.put("host", request.getRemoteHost());
        json.put("port", request.getRemotePort());

        MessageDto message = new MessageDto();
        message.setMessage(requestBody);
        message.setAuthor("behzad_fz");
        message.setComposeTime(LocalDateTime.now());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String payload = objectMapper.writeValueAsString(message);


        producer.send(json.toString(), "pingTopic");
        producer.send(payload, "pingTopic");

        return ResponseEntity.ok(json.toString());
    }
}
