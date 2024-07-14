package com.learny.scheduleservice.configs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Getter
    private String receivedToken;

    @KafkaListener(topics = "student-auth-token", groupId = "student_consumer")
    public void listenStudentToken(String message) {
        this.receivedToken = message;
        System.out.println("Received student token: " + message);
    }

    @KafkaListener(topics = "teacher-auth-service", groupId = "teacher_consumer")
    public void listenTeacherToken(String message) {
        this.receivedToken = message;
        System.out.println("Received teacher token: " + message);
    }

    public void requestStudentToken(String username) {
        kafkaTemplate.send("student-auth-token", username, "request_token");
    }

    public void requestTeacherToken(String username) {
        kafkaTemplate.send("teacher-auth-token", username, "request_token");
    }

    public void cleanReceivedToken() { this.receivedToken = null;}
}
