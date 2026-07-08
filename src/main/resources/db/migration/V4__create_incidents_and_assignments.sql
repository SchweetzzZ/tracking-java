CREATE TABLE incidents (
    id BIGSERIAL PRIMARY KEY,
    location VARCHAR(255),
    description VARCHAR(255),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    priority INTEGER,
    status VARCHAR(50),
    type VARCHAR(50)
);

CREATE TABLE assignments (
    id BIGSERIAL PRIMARY KEY,
    incident_id BIGINT NOT NULL,
    vehicle_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    assigned_at TIMESTAMP NOT NULL,
    accepted_at TIMESTAMP,
    arrived_at TIMESTAMP,
    completed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_assignment_incident FOREIGN KEY (incident_id) REFERENCES incidents(id) ON DELETE CASCADE,
    CONSTRAINT fk_assignment_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE
);
