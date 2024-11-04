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

    @Value("${ollama.api.base-url}")
    private String ollamaApiBaseUrl;

    public MessageConsumerService(ObjectMapper objectMapper, @Value("${ollama.api.base-url}") String ollamaApiBaseUrl) {
        this.webClient = WebClient.builder().baseUrl(ollamaApiBaseUrl).build(); // Utilisation de l'URL configurable
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "textGenerationQueue")
    public void consumeMessage(String messageJson) {
        try {
            GenerationRequest request = objectMapper.readValue(messageJson, GenerationRequest.class);

            // Appel au service Ollama pour générer le texte
            String generatedText = webClient.post()
                    .uri("/api/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new OllamaPromptRequest("qwen2:0.5b", request.getPrompt()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("Generated Text: " + generatedText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToOrchestrator(String requestId, String generatedText) {
        webClient.post()
                .uri(orchestratorUrl + "/response/text")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new OrchestratorRequest(requestId, generatedText))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    private static class OrchestratorRequest {
        private String requestId;
        private String generatedText;

        public OrchestratorRequest(String requestId, String generatedText) {
            this.requestId = requestId;
            this.generatedText = generatedText;
        }
    }

    private static class OllamaPromptRequest {
        @JsonProperty("model")
        private String model;

        @JsonProperty("prompt")
        private String prompt;

        @JsonProperty("stream")
        private boolean stream;

        public OllamaPromptRequest() {
        }

        public OllamaPromptRequest(String model, String prompt) {
            this.model = model;
            this.prompt = prompt;
            this.stream = false;
        }
    }
}
