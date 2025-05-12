package com.example.property_finder.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LocationDTO {
    private Long id;
    private String name;
    private String description;
    private String type;
    private Long parentLocationId;
    private String parentLocationName;
    private List<LocationDTO> subLocations;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;

    // Default constructor
    public LocationDTO() {
        this.subLocations = new ArrayList<>();
    }

    // Constructor for creating new location
    public LocationDTO(String name, String description, String type,
                       Long parentLocationId, Double latitude, Double longitude) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.parentLocationId = parentLocationId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.subLocations = new ArrayList<>();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getParentLocationId() {
        return parentLocationId;
    }

    public void setParentLocationId(Long parentLocationId) {
        this.parentLocationId = parentLocationId;
    }

    public String getParentLocationName() {
        return parentLocationName;
    }

    public void setParentLocationName(String parentLocationName) {
        this.parentLocationName = parentLocationName;
    }

    public List<LocationDTO> getSubLocations() {
        return subLocations;
    }

    public void setSubLocations(List<LocationDTO> subLocations) {
        this.subLocations = subLocations;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}