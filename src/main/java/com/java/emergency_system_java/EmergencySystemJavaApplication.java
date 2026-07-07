package com.java.emergency_system_java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmergencySystemJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmergencySystemJavaApplication.class, args);
	}

}

