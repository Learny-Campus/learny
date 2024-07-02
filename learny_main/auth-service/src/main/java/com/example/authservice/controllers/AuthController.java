package com.example.authservice.controllers;

import com.example.authservice.configs.kafka.KafkaProducer;
import com.example.authservice.dtos.JwtRequest;
import com.example.authservice.dtos.JwtResponse;
import com.example.authservice.dtos.StudentRegistrationDto;
import com.example.authservice.dtos.TeacherRegistrationDto;
import com.example.authservice.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping()
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final KafkaProducer kafkaProducer;

    @PostMapping("/registration/student")
    public ResponseEntity<?> registerStudent(@RequestBody StudentRegistrationDto studentRegistrationDto) {
        return authService.createUser("student", studentRegistrationDto);
    }

    @PostMapping("/registration/teacher")
    public ResponseEntity<?> registerTeacher(@RequestBody TeacherRegistrationDto teacherRegistrationDto) {
        return authService.createUser("teacher", teacherRegistrationDto);
    }

    @PostMapping("/logging/student")
    public ResponseEntity<?> authenticateStudent(@RequestBody JwtRequest authRequest) {
        ResponseEntity<?> response = authService.createAuthToken("student", authRequest);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() instanceof JwtResponse) {
            JwtResponse jwtResponse = (JwtResponse) response.getBody();
            kafkaProducer.send(authRequest.getUsername(), jwtResponse.getToken());
        }
        return response;
    }

    @PostMapping("/logging/teacher")
    public ResponseEntity<?> authenticateTeacher(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken("teacher", authRequest);
    }

    @GetMapping("/info")
    public String userData(Principal principal) {
        return principal.getName();
    }
}

