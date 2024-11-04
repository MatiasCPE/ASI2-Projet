package org.example.imagegenerationservice.controller;


import org.example.imagegenerationservice.model.ImageGenerationRequest;
import org.example.imagegenerationservice.service.MessageQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/image")
public class ImageGenerationController {

    @Autowired
    private MessageConsumerService messageConsumerService;

    @PostMapping
    public ResponseEntity<String> receiveImageGenerationRequest(@Validated @RequestBody ImageGenerationRequest request) {
        // Envoie la demande dans ActiveMQ
        messageQueueService.sendMessageToQueue(request);
        return ResponseEntity.accepted().body("Request received with ID: " + request.getRequestId());
    }
}