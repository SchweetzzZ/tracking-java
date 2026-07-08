
package com.java.emergency_system_java.entity;

import com.java.emergency_system_java.services.incident.Enum.AssigmentStatusEnum;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Assignment implements Serializable {
    public static final long serialVersionUID=1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne
    @JoinColumn(name = "incident_id", nullable = false)
    private Incident incidentId;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicleId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssigmentStatusEnum status;

    @Column(nullable = false)
    private LocalDateTime assignedAt = LocalDateTime.now();
    private LocalDateTime acceptedAt;
    private LocalDateTime arrivedAt;
    private LocalDateTime completedAt;
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Assignment(){};

    public Assignment(String id, Incident incidentId, Vehicle vehicleId, AssigmentStatusEnum status, LocalDateTime assignedAt, LocalDateTime acceptedAt, LocalDateTime arrivedAt, LocalDateTime completedAt, LocalDateTime createdAt) {
        this.id = id;
        this.incidentId = incidentId;
        this.vehicleId = vehicleId;
        this.status = status;
        this.assignedAt = assignedAt;
        this.acceptedAt = acceptedAt;
        this.arrivedAt = arrivedAt;
        this.completedAt = completedAt;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Incident getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(Incident incidentId) {
        this.incidentId = incidentId;
    }

    public Vehicle getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Vehicle vehicleId) {
        this.vehicleId = vehicleId;
    }

    public AssigmentStatusEnum getStatus() {
        return status;
    }

    public void setStatus(AssigmentStatusEnum status) {
        this.status = status;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public LocalDateTime getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(LocalDateTime acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public LocalDateTime getArrivedAt() {
        return arrivedAt;
    }

    public void setArrivedAt(LocalDateTime arrivedAt) {
        this.arrivedAt = arrivedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
