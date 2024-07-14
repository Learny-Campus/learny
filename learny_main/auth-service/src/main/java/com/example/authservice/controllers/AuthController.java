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

    //TODO: разобраться почему студенты могут авторизоваться как преподаватели и наоборот.


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
        authRequest.setRole("STUDENT");
        return authenticateUser("student", authRequest);
    }

    @PostMapping("/logging/teacher")
    public ResponseEntity<?> authenticateTeacher(@RequestBody JwtRequest authRequest) {
        authRequest.setRole("TEACHER");
        return authenticateUser("teacher", authRequest);
    }

    private ResponseEntity<?> authenticateUser(String expectedRole, JwtRequest authRequest) {
        ResponseEntity<?> response = authService.createAuthToken(expectedRole, authRequest);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() instanceof JwtResponse jwtResponse) {
            kafkaProducer.send(authRequest.getUsername(), jwtResponse.getToken());
        }
        return response;
    }

    @GetMapping("/info")
    public String userData(Principal principal) {
        return principal.getName();
    }
}
