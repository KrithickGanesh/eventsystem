package com.example.eventsystem.service;

import com.example.eventsystem.model.Event;
import com.example.eventsystem.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository repo;

    public EventService(EventRepository repo) {
        this.repo = repo;
    }

    public List<Event> getAllEvents() {
        return repo.findAll();
    }

    public Optional<Event> getEventById(int id) {
        return repo.findById(id);
    }

    public Event createEvent(Event event) {
        return repo.save(event);
    }

    public void deleteEvent(int id) {
        repo.deleteById(id);
    }
}
