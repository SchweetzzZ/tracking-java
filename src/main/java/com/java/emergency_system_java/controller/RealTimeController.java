package com.java.emergency_system_java.controller;

import com.java.emergency_system_java.services.kafka.KafkaProducerService;
import com.java.emergency_system_java.services.realTime.RealTimeService;
import com.java.emergency_system_java.services.tracking.Dto.request.TrackingDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class RealTimeController {
    private final KafkaProducerService kafkaService;

    public RealTimeController(KafkaProducerService kafkaService){
        this.kafkaService = kafkaService;
    }

    @MessageMapping("/tracking_localization")
    public void handleTrackingLocalization(@Payload TrackingDto payload){
        System.out.println("WebSocket recebido do cliente: " + payload);
        this.kafkaService.publishLocation(payload);
    }
}
