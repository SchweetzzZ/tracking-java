package com.java.emergency_system_java.entity;

import com.java.emergency_system_java.services.incident.Enum.AssigmentStatusEnum;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Assigment implements Serializable {
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
}
