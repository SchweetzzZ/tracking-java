package com.java.emergency_system_java.entity;

import com.java.emergency_system_java.services.vehicles.Enum.StatusEnum;
import com.java.emergency_system_java.services.vehicles.Enum.TypeEnum;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "vehicles")
public class Vehicle implements Serializable {
    public static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String name;

    public String plate;

    @Enumerated(EnumType.STRING)
    public TypeEnum type;

    @Enumerated(EnumType.STRING)
    public StatusEnum status;

    public Double latitude;
    public Double longitude;

    public Double speed;

    public Boolean trackingEnable = false;
    public Instant lastseen;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Assigment> assigments = new ArrayList<>();

    public Vehicle(){}

    public Vehicle(Long id, String name, String plate, TypeEnum type, StatusEnum status, Double longitude, Double latitude, Double speed, Boolean trackingEnable, Instant lastseen) {
        this.id = id;
        this.name = name;
        this.plate = plate;
        this.type = type;
        this.status = status;
        this.longitude = longitude;
        this.latitude = latitude;
        this.speed = speed;
        this.trackingEnable = trackingEnable;
        this.lastseen = lastseen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Boolean getTrackingEnable() {
        return trackingEnable;
    }

    public void setTrackingEnable(Boolean trackingEnable) {
        this.trackingEnable = trackingEnable;
    }

    public Instant getLastseen() {
        return lastseen;
    }

    public void setLastseen(Instant lastseen) {
        this.lastseen = lastseen;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(getId(), vehicle.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
