package com.example.eventsystem.controller;

import com.example.eventsystem.model.Event;
import com.example.eventsystem.model.Student;
import com.example.eventsystem.repository.StudentRepository;
import com.example.eventsystem.service.EventService;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*") // allow frontend later
public class EventController {
    private final EventService service;
    private final StudentRepository studentRepo;

    public EventController(EventService service, StudentRepository studentRepo) {
        this.service = service;
        this.studentRepo = studentRepo;
    }

    @GetMapping
    public List<Event> getAll() {
        return service.getAllEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getById(@PathVariable int id) {
        return service.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Event> create(@RequestBody Event event) {
        Event created = service.createEvent(event);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    // Export registered students for an event as an XLSX file (live data)
    @GetMapping("/{id}/export")
    public ResponseEntity<byte[]> exportEventRegistrations(@PathVariable int id) throws Exception {
        Event event = service.getEventById(id).orElse(null);
        if (event == null) return ResponseEntity.notFound().build();

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Registrations");

            int rownum = 0;
            Row header = sheet.createRow(rownum++);
            header.createCell(0).setCellValue("Student ID");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Email");

            for (String sid : event.getRegisteredStudentIds()) {
                Row row = sheet.createRow(rownum++);
                row.createCell(0).setCellValue(sid);
                Student s = studentRepo.findById(sid).orElse(null);
                if (s != null) {
                    row.createCell(1).setCellValue(s.getName());
                    row.createCell(2).setCellValue(s.getEmail());
                } else {
                    row.createCell(1).setCellValue("(unknown)");
                    row.createCell(2).setCellValue("");
                }
            }

            // autosize columns
            for (int i = 0; i < 3; i++) sheet.autoSizeColumn(i);

            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            workbook.write(bos);
            byte[] bytes = bos.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=event_" + id + "_registrations.xlsx");

            return ResponseEntity.ok().headers(headers).body(bytes);
        }
    }
}