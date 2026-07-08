package com.java.emergency_system_java.services.realTime;

import com.java.emergency_system_java.services.rabbitMQ.Dto.RabbitDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class RealTimeService {
    private final SimpMessagingTemplate messagingTemplate;

    public RealTimeService(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyDispatch(RabbitDto dto){
        String destination = "/topic/vehicle." + dto.vehicleId() + ".dispatch";
        // Convertemos explicitamente o destino para String usando o método que não gera ambiguidade
        messagingTemplate.convertAndSend((String) destination, (Object) dto);
        System.out.println("WebSocket: Despacho enviado para o veículo " + dto.vehicleId());
    }

    public void emitVechileLocationUpdate(Long vehicleId, Double latitude, Double longitude,
                                          Double speed, Double heading, Double accuracy){
        String destination = "/topic/vehicle." + vehicleId + ".location";

        Map<String, Object> message = new HashMap<>();
        message.put("vehicleId", vehicleId);
        message.put("latitude", latitude);
        message.put("longitude", longitude);
        message.put("speed", speed);
        message.put("heading", heading);
        message.put("accuracy", accuracy);
        message.put("timestamp", Instant.now().toString());

        // Passando um mapa vazio de cabeçalhos (headers) no terceiro parâmetro tira a dúvida do Java!
        messagingTemplate.convertAndSend(destination, message, Map.of());
        System.out.println("WebSocket: Localização enviada para o veículo " + vehicleId);
    }
}
