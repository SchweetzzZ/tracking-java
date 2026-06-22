package com.java.emergency_system_java.services.tracking;

import com.java.emergency_system_java.entity.Vehicle;
import com.java.emergency_system_java.repository.VehicleRepository;
import com.java.emergency_system_java.services.kafka.KafkaProducerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TrackingScheduler {

    private final VehicleRepository vehicleRepository;
    private final KafkaProducerService kafkaProducerService;

    public TrackingScheduler(VehicleRepository vehicleRepository, KafkaProducerService kafkaProducerService) {
        this.vehicleRepository = vehicleRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    // Roda a cada 60 segundos (60000 milissegundos)
    @Scheduled(fixedRate = 60000)
    public void disableInactiveVehicles() {
        // Define a linha de corte: 5 minutos atrás
        Instant threshold = Instant.now().minus(5, ChronoUnit.MINUTES);

        // Busca no repositório os veículos que estão ativos mas o lastseen ficou velho
        List<Vehicle> inactiveVehicles = vehicleRepository.findByTrackingEnableTrueAndLastseenBefore(threshold);

        if (!inactiveVehicles.isEmpty()) {
            System.out.println("[Scheduler] Detectados " + inactiveVehicles.size() + " veículos sem sinal.");

            for (Vehicle vehicle : inactiveVehicles) {
                // 1. Desliga o rastreio no banco
                vehicle.setTrackingEnable(false);
                vehicleRepository.save(vehicle);

                // 2. Dispara um alerta para a fila do Kafka informando a perda do sinal
                Map<String, Object> payload = new HashMap<>();
                payload.put("vehicleId", vehicle.getId());
                payload.put("reason", "Sem atualizações de GPS por mais de 5 minutos");

                this.kafkaProducerService.publishAuditEvent("TRACKING_SIGNAL_LOST", payload);
            }
        }
    }
}
