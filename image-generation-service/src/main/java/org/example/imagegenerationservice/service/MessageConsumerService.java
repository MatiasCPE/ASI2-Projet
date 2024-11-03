package org.example.imagegenerationservice.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.example.imagegenerationservice.model.GenerationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MessageConsumerService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${orchestrator.url}")
    private String orchestratorUrl;

    public MessageConsumerService(ObjectMapper objectMapper) {
        this.webClient = WebClient.builder().baseUrl("http://localhost:8080").build();// Neural Love API base URL
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "imageGenerationQueue")
    public void consumeMessage(String messageJson) { // Recevoir JSON sous forme de String
        try {
            GenerationRequest request = objectMapper.readValue(messageJson, GenerationRequest.class); // Conversion JSON
                                                                                                      // vers objet

            // Appel à Neural Love pour générer l'image
            String generatedImage = webClient.post()
                    .uri("/prompt/req")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new NeuralLovePromptRequest(request.getPrompt()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Affichage du résultat
            System.out.println("Generated Image: " + generatedImage);

        } catch (Exception e) {
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

        // Getters et Setters (peuvent être ajoutés si nécessaire)
    }

    // Classe interne pour formater la requête vers Ollama
    private static class NeuralLovePromptRequest {
        @JsonProperty("promptTxt")
        private String promptTxt;

        @JsonProperty("negativePromptTxt")
        private String negativePromptTxt;

        // Constructeur par défaut requis pour la sérialisation
        public NeuralLovePromptRequest() {
        }

        public NeuralLovePromptRequest(String promptTxt) {
            this.promptTxt = promptTxt;
            this.negativePromptTxt = "";
        }
    }
}