package org.example.imagegenerationservice;

import org.example.imagegenerationservice.model.ImageGenerationRequest;
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

        // Crée une requête et l'envoie dans la file d'attente ActiveMQ
        ImageGenerationRequest request = new ImageGenerationRequest();
        request.setRequestId(requestId);
        request.setPrompt(prompt);

        System.out.println("Sending message to ActiveMQ with Request ID: " + requestId);
        messageQueueService.sendMessageToQueue(request);
    }
}
