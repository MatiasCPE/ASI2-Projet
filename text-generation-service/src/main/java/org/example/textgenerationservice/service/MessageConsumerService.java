package org.example.textgenerationservice.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.textgenerationservice.model.GenerationRequest;
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
        this.webClient = WebClient.builder().baseUrl("http://localhost:11434").build();// Ollama API base URL
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "textGenerationQueue")
    public void consumeMessage(String messageJson) {  // Recevoir JSON sous forme de String
        try {
            GenerationRequest request = objectMapper.readValue(messageJson, GenerationRequest.class);  // Conversion JSON vers objet

            // Appel à Ollama pour générer le texte
            String generatedText = webClient.post()
                    .uri("/api/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new OllamaPromptRequest("qwen2:0.5b", request.getPrompt()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Affichage du résultat
            System.out.println("Generated Text: " + generatedText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendToOrchestrator(String requestId, String generatedText) {
        // Crée l'URL complète de l'orchestrateur en utilisant le endpoint pour recevoir les données
        webClient.post()
                .uri(orchestratorUrl + "/api/v1/receive-generated-text")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new OrchestratorRequest(requestId, generatedText))
                .retrieve()
                .bodyToMono(Void.class)
                .block();  // Synchrone ici, peut être asynchrone si nécessaire
    }

    // Classe interne pour formater la requête vers l'orchestrateur
    private static class OrchestratorRequest {
        private String requestId;
        private String generatedText;

        public OrchestratorRequest(String requestId, String generatedText) {
            this.requestId = requestId;
            this.generatedText = generatedText;
        }

        // Getters et Setters (peuvent être ajoutés si nécessaire)
    }

    // Classe interne pour formater la requête vers Ollama
    private static class OllamaPromptRequest {
        @JsonProperty("model")
        private String model;

        @JsonProperty("prompt")
        private String prompt;

        @JsonProperty("stream")
        private boolean stream;

        // Constructeur par défaut requis pour la sérialisation
        public OllamaPromptRequest() {
        }

        public OllamaPromptRequest(String model, String prompt) {
            this.model = model;
            this.prompt = prompt;
            this.stream = false;
        }
    }
}
