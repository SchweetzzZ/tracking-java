package com.java.emergency_system_java.services.incident;

import com.java.emergency_system_java.entity.Incident;
import com.java.emergency_system_java.repository.IncidentRepository;
import com.java.emergency_system_java.services.exceptions.ResourceNotFoundException;
import com.java.emergency_system_java.services.incident.Dto.request.IncidentDto;
import com.java.emergency_system_java.services.incident.Dto.request.IncidentUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentService {

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

        return repository.save(incident);
    }

    public Incident updateData(Long id, IncidentUpdateDto dto) {
        try {
            Incident incident = repository.getReferenceById(id);
            updateIncidentData(incident, dto);
            return repository.save(incident);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    public void updateIncidentData(Incident incident, IncidentUpdateDto dto){
        if(dto.description() != null){
            incident.setDescription(dto.description());
        }
        if(dto.priority() != null){
            incident.setPriority(dto.priority());
        }
        if(dto.location() != null){
            incident.setLocation(dto.location());
        }
        if(dto.type() != null){
            incident.setType(dto.type());
        }
        if(dto.latitude() != null){
            incident.setLatitude(dto.latitude());
        }
        if(dto.longitude() != null){
            incident.setLongitude(dto.longitude());
        }
        if(dto.status() != null){
            incident.setStatus(dto.status());
        }
    }

    public void deleteIncident(Long id){
        Incident incident = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        repository.delete(incident);
    }

    public Incident getById(Long id){
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<Incident> findAll(){
        return repository.findAll();
    }
}
