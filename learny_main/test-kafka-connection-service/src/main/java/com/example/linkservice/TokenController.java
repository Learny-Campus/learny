package com.example.linkservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {
    private final KafkaConsumer kafkaConsumer;

    @GetMapping("/request-token/student/{username}")
    public ResponseEntity<String> requestStudentToken(@PathVariable String username) {
        kafkaConsumer.cleanReceivedToken();
        kafkaConsumer.requestStudentToken(username);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        String token = kafkaConsumer.getReceivedToken();
        if (!token.isEmpty()) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Student token not received in time");
        }
    }

    @GetMapping("/request-token/teacher/{username}")
    public ResponseEntity<String> requestTeacherToken(@PathVariable String username) {
        kafkaConsumer.cleanReceivedToken();
        kafkaConsumer.requestTeacherToken(username);

        // Подождем немного, чтобы токен успел прийти
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String token = kafkaConsumer.getReceivedToken();
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Teacher token not received in time");
        }
    }
}
