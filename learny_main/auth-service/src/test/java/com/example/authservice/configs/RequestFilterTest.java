package com.example.authservice.configs;

import com.example.authservice.configs.kafka.KafkaConsumer;
import com.example.authservice.entities.Role;
import com.example.authservice.entities.Student;
import com.example.authservice.entities.Teacher;
import com.example.authservice.repositories.StudentRepository;
import com.example.authservice.repositories.TeacherRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RequestFilterTest {
    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private TeacherRepository teacherRepository;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @Test
    @DirtiesContext
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
    @DirtiesContext
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
