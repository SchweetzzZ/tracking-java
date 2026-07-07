package com.java.emergency_system_java.controller;

import com.java.emergency_system_java.entity.Telemetry;
import com.java.emergency_system_java.entity.Vehicle;
import com.java.emergency_system_java.services.tracking.TrackingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tracking")
public class TrackingController {

    private final TrackingService trackingService;

    public TrackingController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @GetMapping("/{vehicleId}/history")
    public ResponseEntity<List<Telemetry>> getHistory(@PathVariable Long vehicleId) {
        List<Telemetry> history = trackingService.getHistory(vehicleId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{vehicleId}/current")
    public ResponseEntity<Vehicle> getCurrentLocation(@PathVariable Long vehicleId) {
        Vehicle vehicle = trackingService.getCurrentLocation(vehicleId);
        return ResponseEntity.ok(vehicle);
    }

    @GetMapping("/{vehicleId}/history/hours")
    public ResponseEntity<List<Telemetry>> getTelemetryHistory(
            @PathVariable Long vehicleId, 
            @RequestParam(defaultValue = "24") int hours) {
        List<Telemetry> history = trackingService.getTelemetryHistory(vehicleId, hours);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{vehicleId}/stats")
    public ResponseEntity<Map<String, Object>> getStats(@PathVariable Long vehicleId) {
        Map<String, Object> stats = trackingService.getStats(vehicleId);
        return ResponseEntity.ok(stats);
    }
}
