package com.java.emergency_system_java.services.incident.Dto.request;

import com.java.emergency_system_java.services.incident.Enum.StatusEnum;
import com.java.emergency_system_java.services.incident.Enum.TypeEnum;
import jakarta.validation.constraints.NotNull;

public record IncidentUpdateDto(
        String location,
        String description,
        Double latitude,
        Double longitude,
        TypeEnum type,
        StatusEnum status,
        int priority
) {
}
