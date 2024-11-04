package org.example.propertycalculationservice.model;

import java.util.Map;

public class CardProperties {
    private String requestId;
    private Map<String, Float> properties;

    // Constructeurs, getters et setters

    public CardProperties() {
    }

    public CardProperties(String requestId, Map<String, Float> properties) {
        this.requestId = requestId;
        this.properties = properties;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Map<String, Float> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Float> properties) {
        this.properties = properties;
    }
}
