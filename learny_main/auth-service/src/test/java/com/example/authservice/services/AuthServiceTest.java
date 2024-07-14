package com.example.authservice.services;

import com.example.authservice.dtos.JwtRequest;
import com.example.authservice.dtos.JwtResponse;
import com.example.authservice.utils.JwtStudentTokenUtils;
import com.example.authservice.utils.JwtTeacherTokenUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AuthServiceTest {
    @MockBean
    private UserService userService;

    @MockBean
    private JwtStudentTokenUtils jwtStudentTokenUtils;

    @MockBean
    private JwtTeacherTokenUtils jwtTeacherTokenUtils;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private AuthService authService;

    @Test
    void testCreateAuthTokenForStudent() {
        String username = "student1";
        String password = "password";
        String token = "jwt-token";

        JwtRequest authRequest = new JwtRequest();
        authRequest.setUsername(username);
        authRequest.setPassword(password);
        authRequest.setRole("STUDENT");

        Authentication authentication = Mockito.mock(Authentication.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);

        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(jwtStudentTokenUtils.generationStudentToken(userDetails, "STUDENT")).thenReturn(token);

        ResponseEntity<?> response = authService.createAuthToken("student", authRequest);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody() instanceof JwtResponse);
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        Assertions.assertEquals(token, jwtResponse.getToken());

        Mockito.verify(kafkaTemplate, Mockito.times(1)).send(Mockito.eq("student-token-response"), Mockito.eq(username), Mockito.eq(token));
    }

    @Test
    void testCreateAuthTokenForTeacher() {
        String username = "teacher1";
        String password = "password";
        String token = "jwt-token";

        JwtRequest authRequest = new JwtRequest();
        authRequest.setUsername(username);
        authRequest.setPassword(password);
        authRequest.setRole("TEACHER");

        Authentication authentication = Mockito.mock(Authentication.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);

        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(jwtTeacherTokenUtils.generateTeacherToken(userDetails, "TEACHER")).thenReturn(token);

        ResponseEntity<?> response = authService.createAuthToken("teacher", authRequest);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody() instanceof JwtResponse);
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        Assertions.assertEquals(token, jwtResponse.getToken());

        Mockito.verify(kafkaTemplate, Mockito.times(1)).send(Mockito.eq("teacher-token-response"), Mockito.eq(username), Mockito.eq(token));
    }
}
