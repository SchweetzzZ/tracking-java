package com.java.emergency_system_java.services.rabbitMQ;

import com.java.emergency_system_java.services.rabbitMQ.Dto.RabbitDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQService(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishDispatch(RabbitDto dto){
        try{
            rabbitTemplate.convertAndSend("dispatch_queue", dto);
            System.out.println("Mensagem enviada com sucesso para dispatch_queue");
        }catch (Exception e){
            System.err.println("Aviso: Falha ao enviar para o RabbitMQ. Mensagem ignorada: " + e.getMessage());
        }
    }
}