package org.example.textgenerationservice;

import org.example.textgenerationservice.model.GenerationRequest;
import org.example.textgenerationservice.service.MessageQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class TextGenerationServiceApplication implements CommandLineRunner {

    @Autowired
    private MessageQueueService messageQueueService;

    public static void main(String[] args) {
        SpringApplication.run(TextGenerationServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Données en dur pour le test
        String requestId = "test-request-id";
        String prompt = "Description for a card of a fictional Pokémon whose characteristics you are free to invent, 100 words maximum";

        // Crée une requête et l'envoie dans la file d'attente ActiveMQ
        GenerationRequest request = new GenerationRequest();
        request.setRequestId(requestId);
        request.setPrompt(prompt);

        System.out.println("Sending message to ActiveMQ with Request ID: " + requestId);
        messageQueueService.sendMessageToQueue(request);
    }
}


