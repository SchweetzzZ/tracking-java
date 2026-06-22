package com.java.emergency_system_java.services.tracking.Dto.request;

import jakarta.validation.constraints.NotNull;


public record TrackingDto(

        @NotNull(message = "vehicle id is required") Long vehicleId,
        @NotNull(message = "latitude is required") Double latitude,
        @NotNull(message = "longitude is required") Double longitude,
        Double speed,
        Double heading,
        Double accuracy
) {
}
