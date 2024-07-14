package com.example.authservice.configs.kafka;

import com.example.authservice.entities.Student;
import com.example.authservice.entities.Teacher;
import com.example.authservice.repositories.StudentRepository;
import com.example.authservice.repositories.TeacherRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${jwt.student_secret}")
    private String studentSecret;

    @Value("${jwt.teacher_secret}")
    private String teacherSecret;

    private void processRequestAndSendMessage(String topic, String username, String role, String secret) {
        String token = Jwts.builder()
                .setSubject(username)
                .claim("roles", role)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        kafkaTemplate.send(topic, username, token);
    }

    @KafkaListener(topics = "student-token-request", groupId = "auth-service")
    public void listenStudentRequest(String username) {
        Optional<Student> student = studentRepository.findStudentByUsername(username);
        if (student.isEmpty()) {
            processRequestAndSendMessage("student-token-response", student.get().getUsername(), student.get().getRoles().toString(), studentSecret);
        }
    }

    @KafkaListener(topics = "teacher-token-request", groupId = "auth-service")
    public void listenTeacherRequest(String username) {
        Optional<Teacher> teacher = teacherRepository.findTeacherByUsername(username);
        if (teacher.isEmpty()) {
            processRequestAndSendMessage("teacher-token-response", teacher.get().getUsername(), teacher.get().getRoles().toString(), teacherSecret);
        }
    }
}

