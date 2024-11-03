package org.example.imagegenerationservice.model;

public class GenerationResponse {
    private String imageUrl;

    public GenerationResponse(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Getters et Setters
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}