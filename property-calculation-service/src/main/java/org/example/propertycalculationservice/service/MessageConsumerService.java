package org.example.propertycalculationservice.service;

import org.example.propertycalculationservice.model.ImageProcessedMessage;
import org.example.propertycalculationservice.model.CardProperties;
import tp.cpe.ImgToProperties; // Assurez-vous que c'est le bon package
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class MessageConsumerService {

    private final ObjectMapper objectMapper;
    private final WebClient webClient;

    @Value("${orchestrator.url}")
    private String orchestratorUrl;

    public MessageConsumerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.webClient = WebClient.builder().build();
    }

    @JmsListener(destination = "imageGeneratedQueue")
    public void consumeMessage(String messageJson) {
        try {
            // Désérialiser le message reçu
            ImageProcessedMessage message = objectMapper.readValue(messageJson, ImageProcessedMessage.class);

            // Récupérer l'URL de l'image
            String imageUrl = message.getImageUrl();

            // Utiliser la librairie pour générer les propriétés
            Map<String, Float> properties = ImgToProperties.getPropertiesFromImg(imageUrl, 100f, 4, 0.3f, true);


            // Créer l'objet CardProperties
            CardProperties cardProperties = new CardProperties(message.getRequestId(), properties);

            // Envoyer les propriétés à l'orchestrateur
            sendToOrchestrator(cardProperties);

        } catch (Exception e) {
            // Gérer les exceptions
            e.printStackTrace();
        }
    }

    private void sendToOrchestrator(CardProperties cardProperties) {
        try {
            webClient.post()
                    .uri(orchestratorUrl + "/api/v1/receive-card-properties")
                    .bodyValue(cardProperties)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            // Gérer les exceptions
            e.printStackTrace();
        }
    }
}
