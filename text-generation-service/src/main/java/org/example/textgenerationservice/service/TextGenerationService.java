package org.example.textgenerationservice.service;

import org.example.textgenerationservice.model.GenerationRequest;
import org.example.textgenerationservice.model.GenerationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TextGenerationService {

    private final WebClient webClient;

    public TextGenerationService() {
        this.webClient = WebClient.builder().baseUrl("http://localhost:11434").build();
    }

    public GenerationResponse generateText(GenerationRequest request) {
        // Exécution de la génération du texte en appelant Ollama
        String result = webClient.post()
                .uri("/api/generate")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();  // Synchronous call, can be adapted to async if needed

        return new GenerationResponse(result);
    }
}