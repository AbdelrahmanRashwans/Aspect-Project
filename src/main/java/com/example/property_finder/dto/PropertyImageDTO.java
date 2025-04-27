package com.example.property_finder.dto;

import java.time.LocalDateTime;

public class PropertyImageDTO {
    private Long id;
    private Long propertyId;
    private String imageUrl;
    private String description;
    private Boolean isPrimary;
    private LocalDateTime createdAt;

    // Default constructor
    public PropertyImageDTO() {
    }

    // Constructor for creating new image
    public PropertyImageDTO(Long propertyId, String imageUrl, String description, Boolean isPrimary) {
        this.propertyId = propertyId;
        this.imageUrl = imageUrl;
        this.description = description;
        this.isPrimary = isPrimary;
    }

    // Constructor for response with full details
    public PropertyImageDTO(Long id, Long propertyId, String imageUrl, String description,
                            Boolean isPrimary, LocalDateTime createdAt) {
        this.id = id;
        this.propertyId = propertyId;
        this.imageUrl = imageUrl;
        this.description = description;
        this.isPrimary = isPrimary;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}