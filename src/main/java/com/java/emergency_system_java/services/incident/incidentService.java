package com.java.emergency_system_java.services.incident;

import com.java.emergency_system_java.entity.Incident;
import com.java.emergency_system_java.repository.IncidentRepository;
import com.java.emergency_system_java.services.incident.Dto.request.IncidentDto;
import com.java.emergency_system_java.services.incident.Dto.request.IncidentUpdateDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class incidentService {


    @Autowired
    private IncidentRepository repository;

    public Incident createIncident(IncidentDto dto){
        Incident incident = new Incident();
        incident.setDescription(dto.description());
        incident.setLatitude(dto.latitude());
        incident.setLongitude(dto.longitude());
        incident.setPriority(dto.priority());
        incident.setStatus(dto.status());
        incident.setType(dto.type());
        incident.setLocation(dto.location());

        Incident createIncident = repository.save(incident);
        return createIncident;
    }

    public Incident updateData(Long id, )

    public Incident updateIncident(IncidentUpdateDto dto, Long id){
        Incident incident = repository.getById(id).orElse

    }
}
