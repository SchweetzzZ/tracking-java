package com.java.emergency_system_java.repository;

import com.java.emergency_system_java.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByTrackingEnableTrueAndLastseenBefore(Instant threshold);
}
