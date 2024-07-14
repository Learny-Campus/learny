package com.example.authservice.services;

import com.example.authservice.exceptions.AppError;
import com.example.authservice.dtos.*;
import com.example.authservice.entities.Student;
import com.example.authservice.entities.Teacher;
import com.example.authservice.repositories.StudentRepository;
import com.example.authservice.repositories.TeacherRepository;
import com.example.authservice.utils.JwtStudentTokenUtils;
import com.example.authservice.utils.JwtTeacherTokenUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtStudentTokenUtils jwtStudentTokenUtils;
    private final JwtTeacherTokenUtils jwtTeacherTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    private static final Logger logger = LoggerFactory.getLogger(PerformanceMonitorInterceptor.class);

    public ResponseEntity<?> createAuthToken(String userType, JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Incorrect login or password"), HttpStatus.UNAUTHORIZED);
        }

        UserDetails userDetails;
        String token;

        if("student".equalsIgnoreCase(userType)) {
            Optional<Student> student = studentRepository.findStudentByUsername(authRequest.getUsername());
            if (student.isPresent()) {
                userDetails = userService.loadUserByUsername(authRequest.getUsername());
                token = jwtStudentTokenUtils.generationStudentToken(userDetails);
                kafkaTemplate.send("student-token-response", authRequest.getUsername(), token);
            } else {
                return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "User not found in student database"), HttpStatus.UNAUTHORIZED);
            }
        } else if ("teacher".equalsIgnoreCase(userType)) {
            Optional<Teacher> teacher = teacherRepository.findTeacherByUsername(authRequest.getUsername());
            if (teacher.isPresent()) {
                userDetails = userService.loadUserByUsername(authRequest.getUsername());
                token = jwtTeacherTokenUtils.generationTeacherToken(userDetails);
                kafkaTemplate.send("teacher-token-response", authRequest.getUsername(), token);
            } else {
                return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "User not found in teacher database"), HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Invalid user type"), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> createUser(String userType, Object registrationDto) {
        if ("student".equals(userType)) {
            StudentRegistrationDto studentRegistrationDto = (StudentRegistrationDto) registrationDto;
            if (!studentRegistrationDto.getPassword().equals(studentRegistrationDto.getConfirmPassword())) {
                return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Passwords do not match"), HttpStatus.BAD_REQUEST);
            }
            if (userService.findStudentByUsername(studentRegistrationDto.getUsername()).isPresent()) {
                return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "A user with this name already exists"), HttpStatus.BAD_REQUEST);
            }
            Student student = userService.createNewStudent(studentRegistrationDto);
            return ResponseEntity.ok(new StudentDto(student.getId(), student.getUsername(), student.getEmail(), 1, "STUDENT"));

        } else if ("teacher".equals(userType)) {
            TeacherRegistrationDto teacherRegistrationDto = (TeacherRegistrationDto) registrationDto;
            if (!teacherRegistrationDto.getPassword().equals(teacherRegistrationDto.getConfirmPassword())) {
                return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Passwords do not match"), HttpStatus.BAD_REQUEST);
            }
            if (userService.findTeacherByUsername(teacherRegistrationDto.getUsername()).isPresent()) {
                return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "A user with this name already exists"), HttpStatus.BAD_REQUEST);
            }
            Teacher teacher = userService.createNewTeacher(teacherRegistrationDto);
            return ResponseEntity.ok(new UserDto(teacher.getId(), teacher.getUsername(), teacher.getEmail(), "TEACHER"));
        } else {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Invalid user type"), HttpStatus.BAD_REQUEST);
        }
    }
}
