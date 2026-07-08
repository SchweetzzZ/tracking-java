package com.java.emergency_system_java.services.dispatch;

import com.java.emergency_system_java.repository.IncidentRepository;

public class DispatchService {

    private final IncidentRepository repository;

    public IncidentService(IncidentRepository repository){
        this.repository = repository;
    }
}
