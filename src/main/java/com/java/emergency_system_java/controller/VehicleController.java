package com.java.emergency_system_java.controller;

import com.java.emergency_system_java.entity.Vehicle;
import com.java.emergency_system_java.services.dto.request.VehicleDto;
import com.java.emergency_system_java.services.dto.request.VehicleUpdateDto;
import com.java.emergency_system_java.services.vehicles.VehicleServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

public class VehicleController {

    @Autowired
    private VehicleServices service;

    @PostMapping
    public ResponseEntity<Vehicle>insert(@Valid @RequestBody VehicleDto dto){
        Vehicle vehicle = service.createdVehicle(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(vehicle.getId())
                .toUri();
        return ResponseEntity.created(uri).body(vehicle);
    }

    @PutMapping
    public ResponseEntity<Vehicle> update(@PathVariable long id, @Valid @RequestBody VehicleUpdateDto dto){
        Vehicle vehicle = service.update(id, dto);
        return ResponseEntity.ok().body(vehicle);
    }

    @DeleteMapping
    public ResponseEntity<Vehicle> delete(@PathVariable long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> findAll(){
        List<Vehicle> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping
    public ResponseEntity<Vehicle> findById(Long id){
        Vehicle vehicle = service.findById(id);
        return ResponseEntity.noContent().build();
    }
}
