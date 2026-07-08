package com.java.emergency_system_java.services.dispatch;

import com.java.emergency_system_java.entity.Assignment;
import com.java.emergency_system_java.entity.Incident;
import com.java.emergency_system_java.entity.Vehicle;
import com.java.emergency_system_java.repository.AssignmentRepository;
import com.java.emergency_system_java.repository.IncidentRepository;
import com.java.emergency_system_java.repository.VehicleRepository;
import com.java.emergency_system_java.services.dispatch.Dto.DispatchDto;
import com.java.emergency_system_java.services.exceptions.ResourceNotFoundException;
import com.java.emergency_system_java.services.kafka.KafkaProducerService;
import com.java.emergency_system_java.services.rabbitMQ.Dto.RabbitDto;
import com.java.emergency_system_java.services.rabbitMQ.RabbitMQService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

public class DispatchService {

    private final IncidentRepository incidentRepository;
    private final VehicleRepository vehicleRepository;
    private final AssignmentRepository assignmentRepository;
    private final RabbitMQService rabbitMQService;
    private final KafkaProducerService kafkaProducerService;

    public DispatchService(IncidentRepository incidentRepository, VehicleRepository vehicleRepository,
                           AssignmentRepository assignmentRepository, RabbitMQService rabbitMQService,
                           KafkaProducerService kafkaProducerService){
        this.incidentRepository = incidentRepository;
        this.vehicleRepository = vehicleRepository;
        this.assignmentRepository = assignmentRepository;
        this.rabbitMQService = rabbitMQService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Transactional
    public Assignment dispatchIncident(DispatchDto dto){
        Incident incident = incidentRepository.findById(dto.incidentId()).orElseThrow(()->
                new ResourceNotFoundException("incident not found"));
        if(!"PENDING".equals(incident.getStatus())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incidente não está pendente");
        }
        Vehicle vehicle = vehicleRepository.findById(dto.vehicleId()).orElseThrow(()->
                new ResourceNotFoundException("vehicle not found"));
        if(!"AVALIABLE".equals(vehicle.getStatus())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "vehicle not avaliable");
        }
        Assignment assignment = new Assignment();
        assignment.setIncidentId(incident);
        assignment.setVehicleId(vehicle);

        incident.setStatus("IN_PROGRESS");
        incidentRepository.save(incident);

        vehicle.setStatus("DISPATCHED");
        vehicleRepository.save(vehicle);

        RabbitDto rabbitDto = new RabbitDto(
                assignment.getId(),
                incident.getId(),
                vehicle.getId()
        );

        KafkaAuditDto auditDto = new KafkaAuditDto(
                assignment.getId(),
                incident.getId(),
                vehicle.getId(),
                LocalDateTime.now()
        );
        kafkaService.publishAuditEvent("DISPATCH_ASSIGNED", auditDto);

        return assignment;
    }
}