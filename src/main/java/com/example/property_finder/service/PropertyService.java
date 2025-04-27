package com.example.property_finder.service;

import com.example.property_finder.model.Property;
import com.example.property_finder.model.User;
import com.example.property_finder.repository.PropertyRepository;
import com.example.property_finder.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    public PropertyService(PropertyRepository propertyRepository, UserRepository userRepository) {
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
    }

    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    public Optional<Property> getPropertyById(Long id) {
        return propertyRepository.findById(id);
    }

    public List<Property> getPropertiesByLocation(String location) {
        return propertyRepository.findByLocation(location);
    }

    public List<Property> getPropertiesByType(String propertyType) {
        return propertyRepository.findByPropertyType(propertyType);
    }

    public List<Property> getPropertiesByListingType(String listingType) {
        return propertyRepository.findByListingType(listingType);
    }

    public List<Property> getPropertiesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return propertyRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Property> getPropertiesByMinBedrooms(Integer bedrooms) {
        return propertyRepository.findByBedroomsGreaterThanEqual(bedrooms);
    }

    public List<Property> getPropertiesByOwner(Long ownerId) {
        return propertyRepository.findByOwnerId(ownerId);
    }

    public List<Property> searchProperties(
            String location,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer bedrooms,
            String propertyType,
            String listingType) {

        return propertyRepository.searchProperties(
                location, minPrice, maxPrice, bedrooms, propertyType, listingType);
    }

    @Transactional
    public Property saveProperty(Property property, Long ownerId) {
        if (ownerId != null) {
            Optional<User> owner = userRepository.findById(ownerId);
            owner.ifPresent(property::setOwner);
        }

        return propertyRepository.save(property);
    }

    @Transactional
    public Optional<Property> updateProperty(Long id, Property propertyDetails) {
        Optional<Property> propertyOptional = propertyRepository.findById(id);

        if (propertyOptional.isPresent()) {
            Property property = propertyOptional.get();

            // Update fields
            property.setTitle(propertyDetails.getTitle());
            property.setDescription(propertyDetails.getDescription());
            property.setPrice(propertyDetails.getPrice());
            property.setLocation(propertyDetails.getLocation());
            property.setBedrooms(propertyDetails.getBedrooms());
            property.setBathrooms(propertyDetails.getBathrooms());
            property.setArea(propertyDetails.getArea());
            property.setPropertyType(propertyDetails.getPropertyType());
            property.setListingType(propertyDetails.getListingType());

            // Don't update owner here to prevent ownership changes

            Property updatedProperty = propertyRepository.save(property);
            return Optional.of(updatedProperty);
        }

        return Optional.empty();
    }

    @Transactional
    public boolean deleteProperty(Long id) {
        Optional<Property> property = propertyRepository.findById(id);

        if (property.isPresent()) {
            propertyRepository.deleteById(id);
            return true;
        }

        return false;
    }
}