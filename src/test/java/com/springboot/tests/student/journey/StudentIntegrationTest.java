package com.springboot.tests.student.journey;

import com.github.javafaker.Faker;
import com.springboot.tests.student.entity.Gender;
import com.springboot.tests.student.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    private static final Faker FAKER = new Faker();
    private final String STUDENT_URI = "/api/v1/students";

    @Test
    void canAddStudent() {
        //create student
        var name = FAKER.name().fullName();
        var email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID() + "@foobar.com";
        var gender = Gender.MALE;

        Student student = new Student(
                name,
                email,
                gender
        );

        //sending post request
        webTestClient.post()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(student), Student.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get all students
        List<Student> studentList = webTestClient.get()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Student>() {
                })
                .returnResult()
                .getResponseBody();

        //get id
        Long id = studentList
                .stream()
                .filter(student1 -> student1.getEmail().equals(email))
                .map(Student::getId)
                .findFirst()
                .orElseThrow();

        student.setId(id);

        //get student by id
        Student actual = webTestClient.get()
                .uri(STUDENT_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Student>() {
                })
                .returnResult()
                .getResponseBody();

        //check if equal
        assertThat(actual).isEqualTo(student);
    }

    @Test
    void canDeleteStudent() {
        //create student
        var name = FAKER.name().fullName();
        var email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID() + "@foobar.com";
        var gender = Gender.MALE;

        Student student = new Student(
                name,
                email,
                gender
        );

        //sending post request
        webTestClient.post()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(student), Student.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get all students
        List<Student> studentList = webTestClient.get()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Student>() {
                })
                .returnResult()
                .getResponseBody();

        //get id
        Long id = studentList
                .stream()
                .filter(student1 -> student1.getEmail().equals(email))
                .map(Student::getId)
                .findFirst()
                .orElseThrow();

        student.setId(id);

        //delete student
        webTestClient.delete()
                .uri(STUDENT_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        //check student not found
        webTestClient.get()
                .uri(STUDENT_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();

    }

    @Test
    void canGetAllStudents() {
        //create student
        var name = FAKER.name().fullName();
        var email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID() + "@foobar.com";
        var gender = Gender.MALE;

        Student student = new Student(
                name,
                email,
                gender
        );

        //sending post request
        webTestClient.post()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(student), Student.class)
                .exchange()
                .expectStatus()
                .isOk();

        //getAllStudents
        List<Student> studentList = webTestClient.get()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Student>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(studentList).isNotEmpty();
    }

    @Test
    void canGetStudent() {
        //create student
        var name = FAKER.name().fullName();
        var email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID() + "@foobar.com";
        var gender = Gender.MALE;

        Student student = new Student(
                name,
                email,
                gender
        );

        //sending post request
        webTestClient.post()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(student), Student.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get all students
        List<Student> studentList = webTestClient.get()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Student>() {
                })
                .returnResult()
                .getResponseBody();

        //get id
        Long id = studentList
                .stream()
                .filter(student1 -> student1.getEmail().equals(email))
                .map(Student::getId)
                .findFirst()
                .orElseThrow();

        //get student by id
        webTestClient.get()
                .uri(STUDENT_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }

}
