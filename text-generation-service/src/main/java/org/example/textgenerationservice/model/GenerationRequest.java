package org.example.textgenerationservice.model;

import java.io.Serializable;

public class GenerationRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String requestId;
    private String prompt;

    // Getters et Setters
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
