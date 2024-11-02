package org.example.textgenerationservice.controller;

import org.example.textgenerationservice.model.GenerationRequest;
import org.example.textgenerationservice.service.MessageQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/text")
public class TextGenerationController {

    @Autowired
    private MessageQueueService messageQueueService;

    @PostMapping
    public String receiveTextGenerationRequest(@RequestBody GenerationRequest request) {
        // Envoie la demande dans l'ActiveMQ
        messageQueueService.sendMessageToQueue(request);
        return "Request received with ID: " + request.getRequestId();
    }
}
