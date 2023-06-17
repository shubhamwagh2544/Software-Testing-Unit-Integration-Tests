package com.springboot.tests.student.service;

import com.springboot.tests.student.entity.Gender;
import com.springboot.tests.student.entity.Student;
import com.springboot.tests.student.exceptions.BadRequestException;
import com.springboot.tests.student.exceptions.StudentNotFoundException;
import com.springboot.tests.student.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    private StudentService underTest;
    @Mock private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentRepository);
    }

    @Test
    void canGetAllStudents() {
        //When
        underTest.getAllStudents();

        //Then
        Mockito.verify(studentRepository).findAll();
    }

    @Test
    void canGetStudentWhenStudentExists() {
        //Given
        long id = 1;
        Student student = new Student(
                id,
                "Shubham Wagh",
                "shubhamwagh@gmail.com",
                Gender.MALE
        );
        underTest.addStudent(student);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        //When
        Student actual = underTest.getStudent(id);

        //Then
        assertThat(actual).isEqualTo(student);
    }

    @Test
    void canThrowExceptionWhenStudentDoesNotExists() {
       //Given
        long id = 1;
        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        //When
        //Then
        assertThatThrownBy(() -> underTest.getStudent(id))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining(String.format("Student with id %s not found", id));
    }

    @Test
    void canAddStudent() {
        //Given
        Student student = new Student(
                "Shubham Wagh",
                "shubhamwagh@gmail.com",
                Gender.MALE
        );

        //When
        underTest.addStudent(student);          // student passed to addStudent()

        //Then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);

        Mockito.verify(studentRepository).save(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void canThrowExceptionWhenStudentEmailIsTaken() {
        //Given
        String email = "shubhamwagh@gmail.com";
        Student student = new Student(
                "Shubham Wagh",
                email,
                Gender.MALE
        );

        given(studentRepository.existsStudentByEmail(email)).willReturn(true);      // given that method returns true

        //When
        //Then
        assertThatThrownBy(() -> underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(String.format("Email %s already exists", email));

        verify(studentRepository, never()).save(student);       //student will never be saved
    }

    @Test
    void canDeleteStudentIfStudentExists() {
        //Given
        long id = 1;
        Student student = new Student(
                id,
                "Shubham Wagh",
                "shubhamwagh@gmail.com",
                Gender.MALE
        );
        underTest.addStudent(student);

        //When
        when(studentRepository.findById(id)).thenReturn(Optional.of(student));
        underTest.deleteStudent(id);

        //Then
        assertThat(studentRepository.existsById(id)).isFalse();
    }

    @Test
    void canThrowExceptionInDeleteStudentWhenStudentDoNotExists() {
        //given
        long id = 1;

        //when
        //then
        assertThatThrownBy(() -> underTest.deleteStudent(id))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining(String.format("Student with id %s not found", id));
    }
}