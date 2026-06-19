package com.java.emergency_system_java.repository;

import com.java.emergency_system_java.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
