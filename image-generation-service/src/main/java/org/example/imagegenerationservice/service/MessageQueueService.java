package org.example.imagegenerationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.imagegenerationservice.model.ImageGenerationRequest;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageQueueService {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public MessageQueueService(JmsTemplate jmsTemplate, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessageToQueue(ImageGenerationRequest request) {
        try {
            String messageJson = objectMapper.writeValueAsString(request);  // Conversion en JSON
            jmsTemplate.convertAndSend("imageGenerationQueue", messageJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}