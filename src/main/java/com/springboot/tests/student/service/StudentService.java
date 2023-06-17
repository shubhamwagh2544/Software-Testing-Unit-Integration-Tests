package com.springboot.tests.student.service;

import com.springboot.tests.student.entity.Student;
import com.springboot.tests.student.exceptions.BadRequestException;
import com.springboot.tests.student.exceptions.StudentNotFoundException;
import com.springboot.tests.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(String.format("Student with id %s not found", id))
                );
    }

    public void addStudent(Student student) {
        String email = student.getEmail();
        boolean b = studentRepository.existsStudentByEmail(email);
        if (!b) {
            Student student1 = new Student(
                    student.getName(),
                    email,
                    student.getGender()
            );
            studentRepository.save(student1);
        }
        else {
            throw new BadRequestException(String.format("Email %s already exists", email));
        }
    }


    public void deleteStudent(Long id) {
        Student student = getStudent(id);
        studentRepository.delete(student);
    }
}
