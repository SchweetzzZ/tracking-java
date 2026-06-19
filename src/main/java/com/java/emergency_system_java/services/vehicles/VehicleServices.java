package com.java.emergency_system_java.services.vehicles;

import com.java.emergency_system_java.entity.Vehicle;
import com.java.emergency_system_java.repository.VehicleRepository;
import com.java.emergency_system_java.services.dto.request.VehicleDto;
import com.java.emergency_system_java.services.dto.request.VehicleUpdateDto;
import com.java.emergency_system_java.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleServices {

    @Autowired
    public VehicleRepository repository;

    public Vehicle createdVehicle(VehicleDto dto){
        Vehicle vehicle = new Vehicle();
        vehicle.setName(dto.name());
        vehicle.setPlate(dto.plate());
        vehicle.setLatitude(dto.latitude());
        vehicle.setLongitude(dto.longitude());
        vehicle.setStatus(dto.status());
        vehicle.setType(dto.type());
        vehicle.setSpeed(dto.speed());
        vehicle.setTrackingEnable(dto.trackingEnabled());
        vehicle.setLastseen(dto.lastSeen());

        Vehicle createdVehicle = repository.save(vehicle);

        return createdVehicle;
    }

    public Vehicle update(Long id, VehicleUpdateDto dto){
        try{
            Vehicle vehicle = repository.getReferenceById(id);
            updateVehicle(vehicle, dto);
            return repository.save(vehicle);
        }catch (jakarta.persistence.EntityNotFoundException e){
            throw new ResourceNotFoundException(id);
        }
    }

    public Vehicle updateVehicle(Vehicle vehicle, VehicleUpdateDto dto){
        if(dto.name() != null){
            vehicle.setName(dto.name());
        }
        if(dto.type() != null){
            vehicle.setType(dto.type());
        }
        if(dto.status() != null) {
            vehicle.setStatus(dto.status());
        }
        if(dto.speed() != null) {
            vehicle.setSpeed(dto.speed());
        }
        if(dto.longitude() != null) {
            vehicle.setLongitude(dto.longitude());
        }
        if(dto.latitude() != null) {
            vehicle.setLatitude(dto.latitude());
        }
        if(dto.trackingEnabled() != null) {
            vehicle.setTrackingEnable(dto.trackingEnabled());
        }
        if(dto.plate() != null) {
            vehicle.setPlate(dto.plate());
        }
        if(dto.lastSeen() != null) {
            vehicle.setLastseen(dto.lastSeen());
        }
        return vehicle;
    }

    public void delete(Long id){
        Vehicle vehicle = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        repository.delete(vehicle);

    }

    public List<Vehicle> findAll() {return repository.findAll();}

    public Vehicle findById(Long id){
        Optional<Vehicle> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
    }
}
