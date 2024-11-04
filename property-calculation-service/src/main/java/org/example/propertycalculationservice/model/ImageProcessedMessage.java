package org.example.propertycalculationservice.model;

public class ImageProcessedMessage {
    private String requestId;
    private String imageUrl;

    // Constructeurs, getters et setters

    public ImageProcessedMessage() {
    }

    public ImageProcessedMessage(String requestId, String imageUrl) {
        this.requestId = requestId;
        this.imageUrl = imageUrl;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
