package org.example.imagegenerationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.imagegenerationservice.model.ImageGenerationRequest;
import org.example.imagegenerationservice.model.NeuralLovePromptRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MessageConsumerService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${orchestrator.url}")
    private String orchestratorUrl;

    @Value("${neural.love.api.url}")
    private String neuralLoveApiUrl;

    public MessageConsumerService(ObjectMapper objectMapper, @Value("${neural.love.api.url}") String neuralLoveApiUrl) {
        this.webClient = WebClient.builder().baseUrl(neuralLoveApiUrl).build();
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "imageGenerationQueue")

    public void consumeMessage(String messageJson) {
        try {
            // Utilisez la classe correcte ici
            ImageGenerationRequest request = objectMapper.readValue(messageJson, ImageGenerationRequest.class);

            NeuralLovePromptRequest promptRequest = new NeuralLovePromptRequest(request.getPrompt());

            // Log the request body
            System.out.println("Sending to Neural Love: " + objectMapper.writeValueAsString(promptRequest));


            // Appel à Neural Love pour générer l'image
            String generatedImage = webClient.post()
                    .uri("/prompt/req")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(promptRequest)
                    .retrieve()
                    .onStatus(status -> status.isError(), clientResponse -> {
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("Neural Love API Error: " + errorBody);
                                    return Mono.error(new RuntimeException("Neural Love API Error: " + errorBody));
                                });
                    })
                    .bodyToMono(String.class)
                    .block();

            // Affichage du résultat
            System.out.println("Generated Image: " + generatedImage);

            // Envoi à l'orchestrateur
            sendToOrchestrator(request.getRequestId(), generatedImage);

        } catch (Exception e) {
            System.err.println("Erreur lors de la consommation du message : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendToOrchestrator(String requestId, String generatedImage) {
        // Crée l'URL complète de l'orchestrateur en utilisant le endpoint pour recevoir
        // les données
        webClient.post()
                .uri(orchestratorUrl + "/response/image")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new OrchestratorRequest(requestId, generatedImage))
                .retrieve()
                .bodyToMono(Void.class)
                .block(); // Synchrone ici, peut être asynchrone si nécessaire
    }

    // Classe interne pour formater la requête vers l'orchestrateur
    private static class OrchestratorRequest {
        private String requestId;
        private String generatedImage;

        public OrchestratorRequest(String requestId, String generatedImage) {
            this.requestId = requestId;
            this.generatedImage = generatedImage;
        }

        // Getters et Setters
        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getGeneratedImage() {
            return generatedImage;
        }

        public void setGeneratedImage(String generatedImage) {
            this.generatedImage = generatedImage;
        }
    }
}
