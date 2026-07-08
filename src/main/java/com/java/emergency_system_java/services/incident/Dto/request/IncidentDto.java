package com.java.emergency_system_java.services.incident.Dto.request;

import com.java.emergency_system_java.services.incident.Enum.IncidentStatus;
import com.java.emergency_system_java.services.incident.Enum.TypeEnum;
import jakarta.validation.constraints.NotNull;

public record IncidentDto(
        @NotNull(message = "deve ter uma location") String location,
        @NotNull(message = "deve ter uma description") String description,
        @NotNull(message = "deve ter uma latitude") Double latitude,
        @NotNull(message = "deve ter uma longitude") Double longitude,
        @NotNull(message = "deve ter um type") TypeEnum type,
        @NotNull(message = "deve ter um status") IncidentStatus status,
        @NotNull(message = "deve ter um valor para priority") int priority

) {
}
