package com.java.emergency_system_java.controller;

import com.java.emergency_system_java.services.kafka.KafkaProducerService;
import com.java.emergency_system_java.services.tracking.Dto.request.TrackingDto;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class TrackingSocketController {

    private final KafkaProducerService kafkaProducerService;

    public TrackingSocketController(KafkaProducerService kafkaProducerService){
        this.kafkaProducerService = kafkaProducerService;
    }

    @MessageMapping("/send-location")
    public void receiveLocation(@Valid TrackingDto data){
        this.kafkaProducerService.publishLocation(data);
    }
}
