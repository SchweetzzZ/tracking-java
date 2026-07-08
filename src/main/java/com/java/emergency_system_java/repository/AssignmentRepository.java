package com.java.emergency_system_java.repository;

import com.java.emergency_system_java.entity.Assignment;
import com.java.emergency_system_java.services.incident.Enum.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByIncidentId(Long incidentId);
    List<Assignment> findByVehicleId(Long vehicleId);
    long countByIncidentIdAndStatusIn(Long incidentId, List<AssignmentStatus> statuses);
}
