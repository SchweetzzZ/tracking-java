package com.java.emergency_system_java.repository;

import com.java.emergency_system_java.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.java.emergency_system_java.services.vehicles.Enum.VehicleStatus;
import java.time.Instant;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByTrackingEnableTrueAndLastseenBefore(Instant threshold);
    List<Vehicle> findByStatus(VehicleStatus status);
}
