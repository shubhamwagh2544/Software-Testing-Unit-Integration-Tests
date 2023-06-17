package com.springboot.tests.student.repository;

import com.springboot.tests.student.entity.Gender;
import com.springboot.tests.student.entity.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StudentRepositoryTest {
    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckWhenStudentEmailExists() {
        //Given
        String email = "shubhamwagh@gmail.com";
        Student student = new Student(
                "Shubham Wagh",
                email,
                Gender.MALE
        );
        underTest.save(student);

        //When
        boolean expected = underTest.existsStudentByEmail(email);

        //Then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckWhenStudentEmailDoesNotExists() {
        //Given
        String email = "shubhamwagh@gmail.com";

        //When
        boolean expected = underTest.existsStudentByEmail(email);       //not saving to DB

        //Then
        assertThat(expected).isFalse();
    }

}
