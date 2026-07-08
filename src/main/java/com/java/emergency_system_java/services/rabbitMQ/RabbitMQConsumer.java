package com.java.emergency_system_java.services.rabbitMQ;

import com.java.emergency_system_java.services.rabbitMQ.Dto.RabbitDto;
import com.java.emergency_system_java.services.realTime.RealTimeService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {
    private final RealTimeService realTimeService;

    public RabbitMQConsumer(RealTimeService realTimeService){
        this.realTimeService = realTimeService;
    }

    @RabbitListener(queues = "dispatch_queue")
    public void consumeDispatchMessage(RabbitDto dto){
        System.out.println("Mensagem recebida do RabbitMQ: " + dto);
        this.realTimeService.notifyDispatch(dto);
    }

}
