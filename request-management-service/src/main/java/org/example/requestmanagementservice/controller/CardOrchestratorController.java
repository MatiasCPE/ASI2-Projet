package org.example.requestmanagementservice.controller;

import org.example.requestmanagementservice.entity.CardRequest;
import org.example.requestmanagementservice.service.CardOrchestratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
public class CardOrchestratorController {

    @Autowired
    private CardOrchestratorService cardOrchestratorService;

    @PostMapping("/create")
    public ResponseEntity<CardRequest> createCard() {
        // Appelle le service pour gérer la création de la carte
        CardRequest cardRequest = cardOrchestratorService.createCardRequest();
        return ResponseEntity.ok(cardRequest); // Retourne la réponse initiale
    }

    @PostMapping("/response/text")
    public ResponseEntity<String> receiveTextResponse(@RequestParam Long cardRequestId,
            @RequestBody String description) {
        // Mettre à jour la demande de carte avec le texte généré
        System.out.println("Received text description: " + description);
        cardOrchestratorService.updateTextAndTriggerImageRequest(cardRequestId, description);
        return ResponseEntity.ok("Text description received and image request triggered");
    }

    @PostMapping("/response/image")
    public ResponseEntity<String> receiveImageResponse(@RequestParam Long cardRequestId, @RequestBody String imageUrl) {
        // Recevoir la réponse du microservice d'image et mettre à jour la demande
        cardOrchestratorService.updateImage(cardRequestId, imageUrl);
        return ResponseEntity.ok("Image URL received and processed successfully");
    }
}
