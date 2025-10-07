package com.example.eventsystem.service;

import com.example.eventsystem.model.Event;
import com.example.eventsystem.model.Student;
import com.example.eventsystem.repository.EventRepository;
import com.example.eventsystem.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepo;
    private final EventRepository eventRepo;

    public StudentService(StudentRepository studentRepo, EventRepository eventRepo) {
        this.studentRepo = studentRepo;
        this.eventRepo = eventRepo;
    }

    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    public Optional<Student> getStudentById(String id) {
        return studentRepo.findById(id);
    }

    public Student createStudent(Student student) {
        return studentRepo.save(student);
    }

    public void deleteStudent(String id) {
        // Remove the student id from any event registrations first
        List<Event> events = eventRepo.findAll();
        for (Event ev : events) {
            if (ev.getRegisteredStudentIds() != null && ev.getRegisteredStudentIds().remove(id)) {
                eventRepo.save(ev);
            }
        }

        studentRepo.deleteById(id);
    }
}
