package org.example.requestmanagementservice.service;

import org.example.requestmanagementservice.entity.CardRequest;
import org.example.requestmanagementservice.repository.CardRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Service
public class CardOrchestratorService {

    @Autowired
    private CardRequestRepository cardRequestRepository;

    @Autowired
    private RestTemplate restTemplate;

    // Champs de classe pour stocker les valeurs
    private Long cardRequestId;
    private String requestId;

    public CardRequest createCardRequest() {
        // Créer une nouvelle demande de carte et définir son statut initial
        CardRequest cardRequest = new CardRequest();
        cardRequest.setStatus("PROCESSING");

        // Enregistrer la demande dans la base de données pour générer un ID
        cardRequest = cardRequestRepository.save(cardRequest);

        // Initialiser cardRequestId et requestId dans les champs de classe
        this.cardRequestId = cardRequest.getId();
        this.requestId = UUID.randomUUID().toString();

        // Lancer le traitement asynchrone de la demande
        new Thread(this::processCardRequest).start();

        return cardRequest;
    }

    private void processCardRequest() {
        try {
            // Débiter l'utilisateur
            boolean debitSuccess = debitUser();
            if (!debitSuccess) {
                updateCardStatus(cardRequestId, "FAILED");
                return;
            }

            // Appeler le service de génération de texte en premier
            String textServiceUrl = "http://text-generation-service/api/generate";
            restTemplate.postForObject(textServiceUrl + "?requestId=" + requestId + "&cardRequestId=" + cardRequestId, null, String.class);

            // Ne pas encore appeler le service d'image ici ; il sera appelé via le contrôleur
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTextAndTriggerImageRequest(Long cardRequestId, String description) {
        // Met à jour le texte généré dans l'objet CardRequest
        Optional<CardRequest> optionalCardRequest = cardRequestRepository.findById(cardRequestId);
        if (optionalCardRequest.isPresent()) {
            CardRequest cardRequest = optionalCardRequest.get();
            cardRequest.setPromptText(description);
            cardRequest.setStatus("TEXT_RECEIVED");
            cardRequestRepository.save(cardRequest);

            // Utiliser le texte pour déclencher l'appel à l'API d'image
            String imageServiceUrl = "http://image-generation-service/api/generate";
            restTemplate.postForObject(imageServiceUrl + "?cardRequestId=" + cardRequestId + "&text=" + description, null, String.class);
        }
    }

    public void updateImage(Long cardRequestId, String imageUrl) {
        // Mettre à jour l'URL de l'image dans la base de données et marquer la demande comme "COMPLETED"
        Optional<CardRequest> optionalCardRequest = cardRequestRepository.findById(cardRequestId);
        if (optionalCardRequest.isPresent()) {
            CardRequest cardRequest = optionalCardRequest.get();
            cardRequest.setPromptImage(imageUrl);
            cardRequest.setStatus("COMPLETED");
            cardRequestRepository.save(cardRequest);
        }
    }

    private boolean debitUser() {
        // Simuler le débit de l'utilisateur
        return true;
    }

    public void updateCardStatus(Long cardRequestId, String status) {
        Optional<CardRequest> optionalCardRequest = cardRequestRepository.findById(cardRequestId);
        if (optionalCardRequest.isPresent()) {
            CardRequest cardRequest = optionalCardRequest.get();
            cardRequest.setStatus(status);
            cardRequestRepository.save(cardRequest);
        }
    }
}
