package com.java.emergency_system_java.services.kafka;

import com.java.emergency_system_java.entity.Telemetry;
import com.java.emergency_system_java.entity.Vehicle;
import com.java.emergency_system_java.repository.TelemetryRepository;
import com.java.emergency_system_java.repository.VehicleRepository;
import com.java.emergency_system_java.services.tracking.Dto.request.TrackingDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class KafkaConsumerService {

    private VehicleRepository vehicleRepository;
    private TelemetryRepository telemetryRepository;
    private KafkaProducerService kafkaProducerService;
    private SimpMessagingTemplate messagingTemplate;

    // construtor manual com tudo dentro
    public KafkaConsumerService(
            VehicleRepository vehicleRepository,
            TelemetryRepository telemetryRepository,
            KafkaProducerService kafkaProducerService,
            SimpMessagingTemplate messagingTemplate) {
        this.vehicleRepository = vehicleRepository;
        this.telemetryRepository = telemetryRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "location-update", groupId = "emergency-group")
    public void handleLocation(TrackingDto data) {
        try {
            Vehicle vehicle = vehicleRepository.findById(data.vehicleId())
                    .orElseThrow(() -> new RuntimeException("Veiculo não encontrado" + data.vehicleId()));
            Boolean wasTrackingDisable = !Boolean.TRUE.equals(vehicle.getTrackingEnable());

            vehicle.setLatitude(data.latitude());
            vehicle.setLongitude(data.longitude());
            vehicle.setTrackingEnable(true);
            vehicle.setLastseen(Instant.now());
            vehicleRepository.save(vehicle);

            if (wasTrackingDisable) {
                Map<String, Object> payload = new HashMap<>();
                payload.put("vehicleId", vehicle.getId());
                this.kafkaProducerService.publishAuditEvent("TRACKING_STARTED", payload);
            }

            Telemetry telemetry = new Telemetry(
                    vehicle,
                    data.latitude(),
                    data.longitude(),
                    data.speed(),
                    data.heading(),
                    data.accuracy());
            telemetryRepository.save(telemetry);

            // união com webSocket
            Map<String, Object> wsPayload = new HashMap<>();
            wsPayload.put("vehicleId", vehicle.getId());
            wsPayload.put("latitude", vehicle.getLatitude());
            wsPayload.put("longitude", vehicle.getLongitude());
            wsPayload.put("speed", vehicle.getSpeed());

            messagingTemplate.convertAndSend("/topic/vehicles", (Object) wsPayload);

            System.out.println("[Kafka Consumer] Processado e enviado para o WebSocket: Veículo " + vehicle.getId());

        } catch (Exception e) {
            System.err.println("[Kafka Consumer] Erro ao processar localização: " + e.getLocalizedMessage());
        }
    }

    @KafkaListener(topics = "audit-events", groupId = "emergency-group")
    public void handleAudit(Map<String, Object> message) {
        // Aqui você pode salvar logs no banco ou disparar alertas de emergência se o
        // evento for grave
        System.out.println("[Kafka Consumer] Log de Auditoria Recebido: " + message);
    }
}
