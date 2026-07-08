package com.java.emergency_system_java.controller;

import com.java.emergency_system_java.entity.Incident;
import com.java.emergency_system_java.services.incident.Dto.request.IncidentDto;
import com.java.emergency_system_java.services.incident.Dto.request.IncidentUpdateDto;
import com.java.emergency_system_java.services.incident.IncidentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/incidents")
public class IncidentController {

    private final IncidentService service;

    public IncidentController(IncidentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Incident> insert(@Valid @RequestBody IncidentDto dto) {
        Incident incident = service.createIncident(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(incident.getId())
                .toUri();
        return ResponseEntity.created(uri).body(incident);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Incident> update(@PathVariable Long id, @Valid @RequestBody IncidentUpdateDto dto) {
        Incident incident = service.updateData(id, dto);
        return ResponseEntity.ok(incident);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteIncident(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Incident>> findAll() {
        List<Incident> list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Incident> findById(@PathVariable Long id) {
        Incident incident = service.getById(id);
        return ResponseEntity.ok(incident);
    }
}
