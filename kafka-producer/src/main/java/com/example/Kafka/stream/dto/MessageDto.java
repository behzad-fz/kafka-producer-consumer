package com.example.Kafka.stream.dto;

import java.time.LocalDateTime;

public class MessageDto {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getComposeTime() {
        return composeTime;
    }

    public void setComposeTime(LocalDateTime composeTime) {
        this.composeTime = composeTime;
    }

    private String message;
    private String author;
    private LocalDateTime composeTime;
}
