package com.springboot.tests.student.repository;

import com.springboot.tests.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsStudentByEmail(String email);

}
