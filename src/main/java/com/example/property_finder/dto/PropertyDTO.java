package com.example.property_finder.dto;

import java.math.BigDecimal;

public class PropertyDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String location;
    private Integer bedrooms;
    private Integer bathrooms;
    private Double area;
    private String propertyType;
    private String listingType;
    private Long ownerId;
    private String ownerName; // For display purposes

    // Default constructor
    public PropertyDTO() {
    }

    // Constructor with fields
    public PropertyDTO(Long id, String title, String description, BigDecimal price, String location,
                       Integer bedrooms, Integer bathrooms, Double area, String propertyType,
                       String listingType, Long ownerId, String ownerName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.location = location;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.area = area;
        this.propertyType = propertyType;
        this.listingType = listingType;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public Integer getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getListingType() {
        return listingType;
    }

    public void setListingType(String listingType) {
        this.listingType = listingType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}