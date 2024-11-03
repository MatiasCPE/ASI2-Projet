package org.example.imagegenerationservice;

import org.example.imagegenerationservice.model.GenerationRequest;
import org.example.imagegenerationservice.service.MessageQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImageGenerationServiceApplication implements CommandLineRunner {

    @Autowired
    private MessageQueueService messageQueueService;

    public static void main(String[] args) {
        SpringApplication.run(ImageGenerationServiceApplication.class, args);
    }

    @Override
    public void run(String... args) {

    }
}
