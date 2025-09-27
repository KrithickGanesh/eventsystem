package com.example.eventsystem.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto increment
    private int id;

    private String name;
    private String description;
    private String date;
    private String venue;
    private int capacity;
    private String organizer;

    // Store list of registered student IDs
    @ElementCollection
    private List<String> registeredStudentIds = new ArrayList<>();

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getOrganizer() { return organizer; }
    public void setOrganizer(String organizer) { this.organizer = organizer; }

    public List<String> getRegisteredStudentIds() { return registeredStudentIds; }
    public void setRegisteredStudentIds(List<String> registeredStudentIds) { this.registeredStudentIds = registeredStudentIds; }
}
