package com.java.emergency_system_java.services.dispatch.Dto;

import com.java.emergency_system_java.services.incident.Enum.AssignmentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateAssignmentStatusDto(
        @NotNull(message = "Status cannot be null")
        AssignmentStatus status
) {
}
