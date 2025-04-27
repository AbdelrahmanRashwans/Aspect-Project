package com.example.property_finder.dto;

import java.time.LocalDateTime;

public class ReviewDTO {
    private Long id;
    private Long propertyId;
    private Long userId;
    private Integer rating;
    private String comment;
    private String userName; // For display purposes
    private String propertyTitle; // For display purposes
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public ReviewDTO() {
    }

    // Constructor for creating new review
    public ReviewDTO(Long propertyId, Long userId, Integer rating, String comment) {
        this.propertyId = propertyId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
    }

    // Constructor for response with full details
    public ReviewDTO(Long id, Long propertyId, Long userId, Integer rating, String comment,
                     String userName, String propertyTitle, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.propertyId = propertyId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.userName = userName;
        this.propertyTitle = propertyTitle;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPropertyTitle() {
        return propertyTitle;
    }

    public void setPropertyTitle(String propertyTitle) {
        this.propertyTitle = propertyTitle;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}