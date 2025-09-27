package com.example.eventsystem.service;

import com.example.eventsystem.model.Student;
import com.example.eventsystem.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository repo;

    public StudentService(StudentRepository repo) {
        this.repo = repo;
    }

    public List<Student> getAllStudents() {
        return repo.findAll();
    }

    public Optional<Student> getStudentById(String id) {
        return repo.findById(id);
    }

    public Student createStudent(Student student) {
        return repo.save(student);
    }

    public void deleteStudent(String id) {
        repo.deleteById(id);
    }
}
