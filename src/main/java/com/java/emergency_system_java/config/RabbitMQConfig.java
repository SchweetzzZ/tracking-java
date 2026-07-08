package com.java.emergency_system_java.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue dispatchQueue() {
        return new Queue("dispatch_queue", true, false, false);
    }
}
