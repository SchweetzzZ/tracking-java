package com.java.emergency_system_java.services.dispatch;

import com.java.emergency_system_java.entity.Assignment;
import com.java.emergency_system_java.entity.Incident;
import com.java.emergency_system_java.entity.Vehicle;
import com.java.emergency_system_java.repository.AssignmentRepository;
import com.java.emergency_system_java.repository.IncidentRepository;
import com.java.emergency_system_java.repository.VehicleRepository;
import com.java.emergency_system_java.services.dispatch.Dto.DispatchDto;
import com.java.emergency_system_java.services.dispatch.Dto.DispatchStatusDto;
import com.java.emergency_system_java.services.exceptions.ResourceNotFoundException;
import com.java.emergency_system_java.services.incident.Enum.AssignmentStatus;
import com.java.emergency_system_java.services.incident.Enum.IncidentStatus;
import com.java.emergency_system_java.services.vehicles.Enum.VehicleStatus;
import com.java.emergency_system_java.services.vehicles.VehicleServices;
import com.java.emergency_system_java.services.kafka.KafkaProducerService;
import com.java.emergency_system_java.services.rabbitMQ.Dto.RabbitDto;
import com.java.emergency_system_java.services.rabbitMQ.RabbitMQService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class DispatchService {

    private final IncidentRepository incidentRepository;
    private final VehicleRepository vehicleRepository;
    private final AssignmentRepository assignmentRepository;
    private final RabbitMQService rabbitMQService;
    private final KafkaProducerService kafkaProducerService;
    private final VehicleServices vehicleServices;

    public DispatchService(IncidentRepository incidentRepository, VehicleRepository vehicleRepository,
                           AssignmentRepository assignmentRepository, RabbitMQService rabbitMQService,
                           KafkaProducerService kafkaProducerService, VehicleServices vehicleServices){
        this.incidentRepository = incidentRepository;
        this.vehicleRepository = vehicleRepository;
        this.assignmentRepository = assignmentRepository;
        this.rabbitMQService = rabbitMQService;
        this.kafkaProducerService = kafkaProducerService;
        this.vehicleServices = vehicleServices;
    }

    @Transactional
    public Assignment dispatchIncident(DispatchDto dto){
        Incident incident = incidentRepository.findById(dto.incidentId()).orElseThrow(()->
                new ResourceNotFoundException("incident not found"));
        if (incident.getStatus() != IncidentStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incidente não está pendente");
        }
        Vehicle vehicle = vehicleRepository.findById(dto.vehicleId()).orElseThrow(()->
                new ResourceNotFoundException("vehicle not found"));
        if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "vehicle not available");
        }
        
        Assignment assignment = new Assignment();
        assignment.setIncident(incident);
        assignment.setVehicle(vehicle);
        assignment.setStatus(AssignmentStatus.ASSIGNED);

        assignment = assignmentRepository.save(assignment);

        incident.setStatus(IncidentStatus.IN_PROGRESS);
        incidentRepository.save(incident);

        vehicle.setStatus(VehicleStatus.DISPACHED);
        vehicleRepository.save(vehicle);

        RabbitDto rabbitDto = new RabbitDto(
                assignment.getId(),
                incident.getId(),
                vehicle.getId()
        );
        rabbitMQService.publishDispatch(rabbitDto);

        Map<String, Object> payload = Map.of(
                "assignmentId", assignment.getId(),
                "incidentId", incident.getId(),
                "vehicleId", vehicle.getId(),
                "timestamp", LocalDateTime.now().toString()
        );
        kafkaProducerService.publishAuditEvent("DISPATCH_ASSIGNED", payload);

        return assignment;
    }

    @Transactional
    public Assignment acceptDispatch(DispatchStatusDto dto) {
        Assignment assignment = assignmentRepository.findById(dto.assignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Despacho não encontrado"));

        if (assignment.getStatus() != AssignmentStatus.ASSIGNED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Despacho não está pendente");
        }

        Vehicle vehicle = vehicleRepository.findById(dto.vehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        assignment.setStatus(AssignmentStatus.ACCEPTED);
        assignment.setAcceptedAt(LocalDateTime.now());
        assignmentRepository.save(assignment);

        vehicle.setStatus(VehicleStatus.EN_ROUTE);
        vehicleRepository.save(vehicle);

        RabbitDto rabbitDto = new RabbitDto(assignment.getId(), assignment.getIncident().getId(), vehicle.getId());
        rabbitMQService.publishDispatch(rabbitDto);

        Map<String, Object> payload = Map.of(
                "assignmentId", assignment.getId(),
                "vehicleId", vehicle.getId(),
                "incidentId", assignment.getIncident().getId(),
                "timestamp", LocalDateTime.now().toString()
        );
        kafkaProducerService.publishAuditEvent("DISPATCH_ACCEPTED", payload);

        return assignment;
    }

    @Transactional
    public Assignment startRoute(DispatchStatusDto dto) {
        Assignment assignment = assignmentRepository.findById(dto.assignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Despacho não encontrado"));

        if (assignment.getStatus() != AssignmentStatus.ACCEPTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Despacho não está aceito");
        }

        Vehicle vehicle = vehicleRepository.findById(dto.vehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        assignment.setStatus(AssignmentStatus.EN_ROUTE);
        assignmentRepository.save(assignment);

        vehicle.setStatus(VehicleStatus.EN_ROUTE);
        vehicleRepository.save(vehicle);

        RabbitDto rabbitDto = new RabbitDto(assignment.getId(), assignment.getIncident().getId(), vehicle.getId());
        rabbitMQService.publishDispatch(rabbitDto);

        Map<String, Object> payload = Map.of(
                "assignmentId", assignment.getId(),
                "vehicleId", vehicle.getId(),
                "incidentId", assignment.getIncident().getId(),
                "timestamp", LocalDateTime.now().toString()
        );
        kafkaProducerService.publishAuditEvent("EN_ROUTE", payload);

        return assignment;
    }

    @Transactional
    public Assignment arrivedAtScene(DispatchStatusDto dto) {
        Assignment assignment = assignmentRepository.findById(dto.assignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Despacho não encontrado"));

        if (assignment.getStatus() != AssignmentStatus.EN_ROUTE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Despacho não está em rota");
        }

        Vehicle vehicle = vehicleRepository.findById(dto.vehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        assignment.setStatus(AssignmentStatus.ARRIVED);
        assignment.setArrivedAt(LocalDateTime.now());
        assignmentRepository.save(assignment);

        vehicle.setStatus(VehicleStatus.AT_INCIDENT);
        vehicleRepository.save(vehicle);

        RabbitDto rabbitDto = new RabbitDto(assignment.getId(), assignment.getIncident().getId(), vehicle.getId());
        rabbitMQService.publishDispatch(rabbitDto);

        Map<String, Object> payload = Map.of(
                "assignmentId", assignment.getId(),
                "vehicleId", vehicle.getId(),
                "incidentId", assignment.getIncident().getId(),
                "timestamp", LocalDateTime.now().toString()
        );
        kafkaProducerService.publishAuditEvent("ARRIVED_AT_SCENE", payload);

        return assignment;
    }

    @Transactional
    public Assignment completedDispatch(DispatchStatusDto dto) {
        Assignment assignment = assignmentRepository.findById(dto.assignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Despacho não encontrado"));

        if (assignment.getStatus() != AssignmentStatus.ARRIVED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Despacho não está no local");
        }

        Vehicle vehicle = vehicleRepository.findById(dto.vehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        Incident incident = assignment.getIncident();

        assignment.setStatus(AssignmentStatus.COMPLETED);
        assignment.setCompletedAt(LocalDateTime.now());
        assignmentRepository.save(assignment);

        vehicle.setStatus(VehicleStatus.AVAILABLE);
        vehicleRepository.save(vehicle);

        long activeAssignments = assignmentRepository.countByIncidentIdAndStatusIn(
                incident.getId(),
                List.of(AssignmentStatus.ASSIGNED, AssignmentStatus.ACCEPTED, AssignmentStatus.EN_ROUTE, AssignmentStatus.ARRIVED)
        );

        if (activeAssignments == 0) {
            incident.setStatus(IncidentStatus.RESOLVED);
            incidentRepository.save(incident);
        }

        RabbitDto rabbitDto = new RabbitDto(assignment.getId(), incident.getId(), vehicle.getId());
        rabbitMQService.publishDispatch(rabbitDto);

        Map<String, Object> payload = Map.of(
                "assignmentId", assignment.getId(),
                "vehicleId", vehicle.getId(),
                "incidentId", incident.getId(),
                "timestamp", LocalDateTime.now().toString()
        );
        kafkaProducerService.publishAuditEvent("DISPATCH_COMPLETED", payload);

        return assignment;
    }

    @Transactional
    public Assignment autoDispatch(Long incidentId) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new ResourceNotFoundException("Incidente não encontrado"));

        List<Vehicle> nearestVehicles = vehicleServices.findNearestVehicule(incident.getLatitude(), incident.getLongitude());

        if (nearestVehicles.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum veículo disponível encontrado");
        }

        Vehicle nearestVehicle = nearestVehicles.get(0);

        return dispatchIncident(new DispatchDto(nearestVehicle.getId(), incident.getId()));
    }

    public Assignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public List<Assignment> getAssignmentsByIncidentId(Long incidentId) {
        return assignmentRepository.findByIncidentId(incidentId);
    }

    public List<Assignment> getAssignmentsByVehicleId(Long vehicleId) {
        return assignmentRepository.findByVehicleId(vehicleId);
    }
}