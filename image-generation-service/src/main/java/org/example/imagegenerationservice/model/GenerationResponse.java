package org.example.imagegenerationservice.model;

public class GenerationResponse {
    private String generatedText;

    public GenerationResponse(String generatedText) {
        this.generatedText = generatedText;
    }

    // Getters et Setters

    public String getGeneratedText() {
        return generatedText;
    }

    public void setGeneratedText(String generatedText) {
        this.generatedText = generatedText;
    }
}