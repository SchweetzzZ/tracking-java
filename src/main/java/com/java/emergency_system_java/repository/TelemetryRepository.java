package com.java.emergency_system_java.repository;

import com.java.emergency_system_java.entity.Telemetry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TelemetryRepository extends JpaRepository<Telemetry, Long> {
    List<Telemetry> findByVehicleIdOrderByCreatedAtAsc(Long vehicleId);
    List<Telemetry> findByVehicleIdAndCreatedAtAfter(Long vehicleId, LocalDateTime fromDate);
}
