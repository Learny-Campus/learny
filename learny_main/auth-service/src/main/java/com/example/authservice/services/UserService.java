package com.example.authservice.services;

import com.example.authservice.configs.BasicSecurityConfig;
import com.example.authservice.dtos.StudentRegistrationDto;
import com.example.authservice.dtos.TeacherRegistrationDto;
import com.example.authservice.entities.Student;
import com.example.authservice.entities.Teacher;
import com.example.authservice.repositories.StudentRepository;
import com.example.authservice.repositories.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final BasicSecurityConfig basicSecurityConfig;

    public Optional<Student> findStudentByUsername(String username) {
        return studentRepository.findStudentByUsername(username);
    }

    public Optional<Teacher> findTeacherByUsername(String username) {
        return teacherRepository.findTeacherByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Teacher> optionalTeacher = teacherRepository.findTeacherByUsername(username);
        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            return new org.springframework.security.core.userdetails.User(
                    teacher.getUsername(),
                    teacher.getPassword(),
                    teacher.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName()))
                            .collect(Collectors.toList())
            );
        }

        Optional<Student> optionalStudent = studentRepository.findStudentByUsername(username);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            return new org.springframework.security.core.userdetails.User(
                    student.getUsername(),
                    student.getPassword(),
                    student.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName()))
                            .toList()
            );
        }

        throw new UsernameNotFoundException(String.format("User %s not found", username));
    }

    public Teacher createNewTeacher(TeacherRegistrationDto teacherRegistrationDto) {
        Teacher teacher = new Teacher();
        teacher.setUsername(teacherRegistrationDto.getUsername());
        teacher.setSecondName(teacherRegistrationDto.getSecondName());
        teacher.setEmail(teacherRegistrationDto.getEmail());
        teacher.setPassword(basicSecurityConfig.passwordEncoder().encode(teacherRegistrationDto.getPassword()));
        teacher.setDegree(teacherRegistrationDto.getDegree());
        teacher.setIndex(teacherRegistrationDto.getIndex());
        return teacherRepository.save(teacher);
    }

    public Student createNewStudent(StudentRegistrationDto studentRegistrationDto) {
        Student student = new Student();
        student.setUsername(studentRegistrationDto.getUsername());
        student.setSecondName(studentRegistrationDto.getSecondName());
        student.setEmail(studentRegistrationDto.getEmail());
        student.setPassword(basicSecurityConfig.passwordEncoder().encode(studentRegistrationDto.getPassword()));
        student.setIndex(studentRegistrationDto.getIndex());
        student.setYearOfStudying(studentRegistrationDto.getYearOfStudying());
        return studentRepository.save(student);
    }
}
