package com.example.Kafka.stream.controller;

import com.example.Kafka.stream.service.Producer;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
    private final Producer producer;

    public PingController(Producer producer) {
        this.producer = producer;
    }

    @PostMapping("/ping")
    public ResponseEntity<String> ping(@RequestBody String requestBody, HttpServletRequest request) {
        JSONObject json = new JSONObject();
        json.put("requestBody", requestBody);
        json.put("userIp", request.getRemoteAddr());
        json.put("userAgent", request.getHeader("User-Agent"));
        json.put("host", request.getRemoteHost());
        json.put("port", request.getRemotePort());

        producer.send(json.toString(), "pingTopic");

        return ResponseEntity.ok(json.toString());
    }
}
