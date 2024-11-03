package org.example.imagegenerationservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NeuralLovePromptRequest {

    @JsonProperty("prompt")
    private String prompt;

    @JsonProperty("negative_prompt")
    private String negativePrompt;

    // Constructeur par défaut requis pour la sérialisation
    public NeuralLovePromptRequest() {
    }

    public NeuralLovePromptRequest(String prompt) {
        this.prompt = prompt;
        this.negativePrompt = "";
    }

    // Getters et Setters
    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getNegativePrompt() {
        return negativePrompt;
    }

    public void setNegativePrompt(String negativePrompt) {
        this.negativePrompt = negativePrompt;
    }
}
