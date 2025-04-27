package com.example.property_finder.dto;

import java.time.LocalDateTime;

public class BookmarkDTO {
    private Long id;
    private Long userId;
    private Long propertyId;
    private PropertyDTO property; // To include property details when fetching bookmarks
    private LocalDateTime createdAt;

    // Default constructor
    public BookmarkDTO() {
    }

    // Constructor for creating a new bookmark
    public BookmarkDTO(Long userId, Long propertyId) {
        this.userId = userId;
        this.propertyId = propertyId;
    }

    // Constructor for response with full details
    public BookmarkDTO(Long id, Long userId, Long propertyId, PropertyDTO property, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.propertyId = propertyId;
        this.property = property;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public PropertyDTO getProperty() {
        return property;
    }

    public void setProperty(PropertyDTO property) {
        this.property = property;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}