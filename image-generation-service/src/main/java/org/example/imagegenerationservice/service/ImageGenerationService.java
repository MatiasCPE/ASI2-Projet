package org.example.imagegenerationservice.service;

import org.example.imagegenerationservice.model.GenerationRequest;
import org.example.imagegenerationservice.model.GenerationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ImageGenerationService {
    private final WebClient webClient;

    public ImageGenerationService() {
        this.webClient = WebClient.builder().baseUrl("http://localhost:8080").build();
    }

    public GenerationResponse generateImageTest(GenerationRequest request) {
        // Exécution de la génération d'image en appelant Neural Love
        String result = webClient.post()
                .uri("/prompt/req")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Synchronous call, can be adapted to async if needed

        return new GenerationResponse(result);
    }
}
