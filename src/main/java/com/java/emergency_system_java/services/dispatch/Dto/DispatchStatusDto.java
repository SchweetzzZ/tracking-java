package com.java.emergency_system_java.services.dispatch.Dto;

import jakarta.validation.constraints.NotNull;

public record DispatchStatusDto(
        @NotNull(message = "assignmentId is required") Long assignmentId,
        @NotNull(message = "vehicleId is required") Long vehicleId
) {}
