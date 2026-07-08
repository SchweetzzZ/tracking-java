package com.java.emergency_system_java.controller;

import com.java.emergency_system_java.entity.Assignment;
import com.java.emergency_system_java.services.dispatch.DispatchService;
import com.java.emergency_system_java.services.dispatch.Dto.DispatchDto;
import com.java.emergency_system_java.services.dispatch.Dto.DispatchStatusDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/dispatches")
public class DispatchController {

    private final DispatchService dispatchService;

    public DispatchController(DispatchService dispatchService) {
        this.dispatchService = dispatchService;
    }

    @PostMapping
    public ResponseEntity<Assignment> dispatchIncident(@Valid @RequestBody DispatchDto dto) {
        Assignment assignment = dispatchService.dispatchIncident(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(assignment.getId())
                .toUri();
        return ResponseEntity.created(uri).body(assignment);
    }

    @PutMapping("/accept")
    public ResponseEntity<Assignment> acceptDispatch(@Valid @RequestBody DispatchStatusDto dto) {
        Assignment assignment = dispatchService.acceptDispatch(dto);
        return ResponseEntity.ok(assignment);
    }

    @PutMapping("/start-route")
    public ResponseEntity<Assignment> startRoute(@Valid @RequestBody DispatchStatusDto dto) {
        Assignment assignment = dispatchService.startRoute(dto);
        return ResponseEntity.ok(assignment);
    }

    @PutMapping("/arrived")
    public ResponseEntity<Assignment> arrivedAtScene(@Valid @RequestBody DispatchStatusDto dto) {
        Assignment assignment = dispatchService.arrivedAtScene(dto);
        return ResponseEntity.ok(assignment);
    }

    @PutMapping("/complete")
    public ResponseEntity<Assignment> completedDispatch(@Valid @RequestBody DispatchStatusDto dto) {
        Assignment assignment = dispatchService.completedDispatch(dto);
        return ResponseEntity.ok(assignment);
    }

    @PostMapping("/auto/{incidentId}")
    public ResponseEntity<Assignment> autoDispatch(@PathVariable Long incidentId) {
        Assignment assignment = dispatchService.autoDispatch(incidentId);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/dispatches/{id}")
                .buildAndExpand(assignment.getId())
                .toUri();
        return ResponseEntity.created(uri).body(assignment);
    }

    @GetMapping
    public ResponseEntity<List<Assignment>> getAll() {
        List<Assignment> assignments = dispatchService.getAllAssignments();
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getById(@PathVariable Long id) {
        Assignment assignment = dispatchService.getAssignmentById(id);
        return ResponseEntity.ok(assignment);
    }

    @GetMapping("/incident/{incidentId}")
    public ResponseEntity<List<Assignment>> getByIncident(@PathVariable Long incidentId) {
        List<Assignment> assignments = dispatchService.getAssignmentsByIncidentId(incidentId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<Assignment>> getByVehicle(@PathVariable Long vehicleId) {
        List<Assignment> assignments = dispatchService.getAssignmentsByVehicleId(vehicleId);
        return ResponseEntity.ok(assignments);
    }
}
