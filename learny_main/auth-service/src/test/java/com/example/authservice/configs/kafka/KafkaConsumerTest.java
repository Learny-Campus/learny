package com.example.authservice.configs.kafka;

import com.example.authservice.configs.kafka.KafkaConsumer;
import com.example.authservice.entities.Role;
import com.example.authservice.entities.Student;
import com.example.authservice.entities.Teacher;
import com.example.authservice.repositories.StudentRepository;
import com.example.authservice.repositories.TeacherRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Collections;
import java.util.Optional;

@SpringJUnitConfig
public class KafkaConsumerTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private KafkaConsumer kafkaConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListenStudentRequest() {
        String username = "student1";
        Student student = new Student();
        student.setUsername(username);
        student.setRoles(Collections.singleton(new Role(1, "ROLE_STUDENT")));

        Mockito.when(studentRepository.findStudentByUsername(username)).thenReturn(Optional.of(student));

        kafkaConsumer.listenStudentRequest(username);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(kafkaTemplate, Mockito.times(1)).send(
                Mockito.eq("student-token-response"), keyCaptor.capture(), messageCaptor.capture());

        Assertions.assertEquals(username, keyCaptor.getValue());
        Assertions.assertNotNull(messageCaptor.getValue());
    }

    @Test
    void testListenTeacherRequest() {
        String username = "teacher1";
        Teacher teacher = new Teacher();
        teacher.setUsername(username);
        teacher.setRoles(Collections.singleton(new Role(1, "ROLE_TEACHER")));

        Mockito.when(teacherRepository.findTeacherByUsername(username)).thenReturn(Optional.of(teacher));

        kafkaConsumer.listenTeacherRequest(username);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(kafkaTemplate, Mockito.times(1)).send(
                Mockito.eq("teacher-token-response"), keyCaptor.capture(), messageCaptor.capture());

        Assertions.assertEquals(username, keyCaptor.getValue());
        Assertions.assertNotNull(messageCaptor.getValue());
    }
}
