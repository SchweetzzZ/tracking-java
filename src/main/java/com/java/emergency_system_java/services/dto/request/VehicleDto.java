package com.java.emergency_system_java.services.dto.request;

import com.java.emergency_system_java.services.Enum.StatusEnum;
import com.java.emergency_system_java.services.Enum.TypeEnum;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record VehicleDto(
        @NotNull(message = "deve ter um nome") String name,
        @NotNull(message = "deve ter a placa do veiculo") String plate,
        @NotNull(message = "deve ter um tipo") TypeEnum type,
        @NotNull(message = "deve ter um status")StatusEnum status,
        @NotNull(message = "deve ter uma latitude") Double latitude,
        @NotNull(message = "deve ter uma longitude") Double longitude,
        @NotNull(message = "deve ter uma velocidade") Double speed,
        @NotNull Boolean trackingEnabled,
        @NotNull Instant lastSeen
        ) {
}
