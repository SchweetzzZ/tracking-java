package com.java.emergency_system_java.services.vehicles.dto.request;

import com.java.emergency_system_java.services.vehicles.Enum.VehicleStatus;
import com.java.emergency_system_java.services.vehicles.Enum.TypeEnum;

import java.time.Instant;

public record VehicleUpdateDto(
        String name,
        String plate,
        TypeEnum type,
        VehicleStatus status,
        Double latitude,
        Double longitude,
        Double speed,
        Boolean trackingEnabled,
        Instant lastSeen
) {}
