package com.java.emergency_system_java.services.rabbitMQ.Dto;

public record RabbitDto(
        Long assignmentId,
        Long incidentId,
        Long vehicleId
) {
}
