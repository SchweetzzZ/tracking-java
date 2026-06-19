package com.java.emergency_system_java.services.dto.request;

import com.java.emergency_system_java.services.Enum.StatusEnum;
import com.java.emergency_system_java.services.Enum.TypeEnum;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record VehicleUpdateDto(
        String name,
        String plate,
        TypeEnum type,
        StatusEnum status,
        Double latitude,
        Double longitude,
        Double speed,
        Boolean trackingEnabled,
        Instant lastSeen
) {}
