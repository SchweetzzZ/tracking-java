package com.java.emergency_system_java.services.kafka;

import com.java.emergency_system_java.services.tracking.Dto.request.TrackingDto;
import com.java.emergency_system_java.services.tracking.Dto.request.TrackingUpdateDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishLocation(TrackingDto data) {
        try {
            this.kafkaTemplate.send("location-update", String.valueOf(data.vehicleId()), data);
            System.out.println("[kafka Producer] location update publicado para o veiculo" + data.vehicleId());
        } catch (Exception e) {
            System.err.println("Erro ao publicar localização no kafka" + e.getMessage());
        }
    }

    public void publishAuditEvent(String eventType, Map<String, Object> payload) {
        Map<String, Object> message = Map.of(
                "eventType", eventType,
                "payload", payload);
        try {
            this.kafkaTemplate.send("audit-events", message);
            System.out.print("[kafka Producer] Evento de auditória publicado" + eventType);
        } catch (Exception e) {
            System.err.print("Erro ao enviar envento de auditória" + e.getMessage());
        }
    }
}
