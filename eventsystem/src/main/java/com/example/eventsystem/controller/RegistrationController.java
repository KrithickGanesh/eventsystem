package com.example.eventsystem.controller;

import com.example.eventsystem.model.Event;
import com.example.eventsystem.model.Student;
import com.example.eventsystem.repository.EventRepository;
import com.example.eventsystem.repository.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "*") // Allow frontend (HTML/JS) requests
public class RegistrationController {

    private final EventRepository eventRepo;
    private final StudentRepository studentRepo;

    public RegistrationController(EventRepository eventRepo, StudentRepository studentRepo) {
        this.eventRepo = eventRepo;
        this.studentRepo = studentRepo;
    }

    // ✅ Register a student for an event
    @PostMapping("/{eventId}/{studentId}")
    public ResponseEntity<String> registerStudent(
            @PathVariable int eventId,
            @PathVariable String studentId) {

        Optional<Event> eventOpt = eventRepo.findById(eventId);
        Optional<Student> studentOpt = studentRepo.findById(studentId);

        if (eventOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Event not found");
        }
        if (studentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Student not found");
        }

        Event event = eventOpt.get();

        // Capacity check
        if (event.getRegisteredStudentIds().size() >= event.getCapacity()) {
            return ResponseEntity.badRequest().body("Event is full");
        }

        // Avoid duplicate registration
        if (event.getRegisteredStudentIds().contains(studentId)) {
            return ResponseEntity.badRequest().body("Student already registered");
        }

        // Register student
        event.getRegisteredStudentIds().add(studentId);
        eventRepo.save(event);

        return ResponseEntity.ok("Student registered successfully!");
    }

    // ❌ Cancel a student's registration
    @DeleteMapping("/{eventId}/{studentId}")
    public ResponseEntity<String> cancelRegistration(
            @PathVariable int eventId,
            @PathVariable String studentId) {

        Optional<Event> eventOpt = eventRepo.findById(eventId);
        if (eventOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Event not found");
        }

        Event event = eventOpt.get();

        if (event.getRegisteredStudentIds().remove(studentId)) {
            eventRepo.save(event); // ✅ persist updated list in DB
            return ResponseEntity.ok("Registration cancelled");
        } else {
            return ResponseEntity.badRequest().body("Student was not registered for this event");
        }
    }
}
