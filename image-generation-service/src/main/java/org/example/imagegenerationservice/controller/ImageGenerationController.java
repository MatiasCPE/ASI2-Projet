package org.example.imagegenerationservice.controller;

import org.example.imagegenerationservice.model.GenerationRequest;
import org.example.imagegenerationservice.service.MessageConsumerService;
import org.example.imagegenerationservice.service.MessageQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/image")
public class ImageGenerationController {

    @Autowired
    private MessageConsumerService messageConsumerService;

    @PostMapping
    public String receiveTextGenerationRequest(@RequestBody GenerationRequest request) {
        // Envoie la demande dans l'ActiveMQ
        // messageQueueService.sendMessageToQueue(request);
        // return "Request received with ID: " + request.getRequestId();
        messageConsumerService.sendToOrchestrator(request.getRequestId(), request.getPrompt());
        return "Request received with ID: " + request.getRequestId();
    }
}