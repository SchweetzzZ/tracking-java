package com.java.emergency_system_java.services.tracking;

import com.java.emergency_system_java.entity.Telemetry;
import com.java.emergency_system_java.entity.Vehicle;
import com.java.emergency_system_java.repository.TelemetryRepository;
import com.java.emergency_system_java.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrackingService {

    private final VehicleRepository vehicleRepository;
    private final TelemetryRepository telemetryRepository;

    // Construtor manual puro para injeção de dependências do Spring
    public TrackingService(VehicleRepository vehicleRepository, TelemetryRepository telemetryRepository) {
        this.vehicleRepository = vehicleRepository;
        this.telemetryRepository = telemetryRepository;
    }

    /**
     * Busca o histórico completo de posições de um veículo ordenado por data.
     */
    public List<Telemetry> getHistory(Long vehicleId) {
        return this.telemetryRepository.findByVehicleIdOrderByCreatedAtAsc(vehicleId);
    }

    /**
     * Busca a posição atualizada do veículo direto da tabela principal.
     */
    public Vehicle getCurrentLocation(Long vehicleId) {
        return this.vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado para rastreamento: " + vehicleId));
    }

    /**
     * Busca o histórico de telemetria das últimas X horas.
     */
    public List<Telemetry> getTelemetryHistory(Long vehicleId, int hours) {
        LocalDateTime fromDate = LocalDateTime.now().minusHours(hours);
        return this.telemetryRepository.findByVehicleIdAndCreatedAtAfter(vehicleId, fromDate);
    }

    /**
     * Calcula estatísticas de velocidade (Média e Máxima) convertendo m/s para km/h.
     */
    public Map<String, Object> getStats(Long vehicleId) {
        // Busca todas as telemetrias do veículo para calcular as métricas
        List<Telemetry> telemetryList = this.telemetryRepository.findByVehicleIdOrderByCreatedAtAsc(vehicleId);

        Map<String, Object> stats = new HashMap<>();

        // Se não houver dados, retorna um mapa com valores zerados/nulos
        if (telemetryList.isEmpty()) {
            stats.put("totalLocations", 0);
            stats.put("firstSignal", null);
            stats.put("lastSignal", null);
            stats.put("averageSpeedKmh", 0.0);
            stats.put("maxSpeedKmh", 0.0);
            return stats;
        }

        // Fator de conversão: Metros por segundo para Quilômetros por hora (3.6)
        double MS_TO_KMH = 3.6;

        // Extrai apenas as velocidades que não são nulas da lista de telemetrias
        List<Double> speeds = new ArrayList<>();
        for (Telemetry t : telemetryList) {
            if (t.getSpeed() != null) {
                speeds.add(t.getSpeed());
            }
        }

        double maxSpeed = 0.0;
        double avgSpeed = 0.0;

        if (!speeds.isEmpty()) {
            maxSpeed = speeds.stream()
                    .mapToDouble(speed -> speed * MS_TO_KMH)
                    .max()
                    .orElse(0.0);

            avgSpeed = speeds.stream()
                    .mapToDouble(speed -> speed * MS_TO_KMH)
                    .average()
                    .orElse(0.0);
        }

        // Pega o primeiro e o último sinal capturados na lista ordenada
        LocalDateTime firstSignal = telemetryList.get(0).getCreatedAt();
        LocalDateTime lastSignal = telemetryList.get(telemetryList.size() - 1).getCreatedAt();

        // Monta o payload de retorno arredondando para 1 casa decimal (.round() do Java)
        stats.put("totalLocations", telemetryList.size());
        stats.put("firstSignal", firstSignal);
        stats.put("lastSignal", lastSignal);
        stats.put("averageSpeedKmh", Math.round(avgSpeed * 10.0) / 10.0);
        stats.put("maxSpeedKmh", Math.round(maxSpeed * 10.0) / 10.0);

        return stats;
    }
}
