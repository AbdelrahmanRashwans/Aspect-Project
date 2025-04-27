package com.example.property_finder.service;

import com.example.property_finder.dto.PropertySearchDTO;
import com.example.property_finder.model.Property;
import com.example.property_finder.repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private final PropertyRepository propertyRepository;

    public SearchService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public List<Property> searchProperties(PropertySearchDTO searchDTO) {
        return propertyRepository.advancedSearch(searchDTO);
    }

    // You could add more specialized search methods here in the future
    public List<Property> findFeaturedProperties() {
        // Logic to find featured properties (perhaps the newest listings or premium listings)
        // For simplicity, just return all properties for now
        return propertyRepository.findAll();
    }

    public List<Property> findSimilarProperties(Long propertyId) {
        // In a real app, this would implement logic to find similar properties
        // For now, just get the property and find others in the same location with similar price
        Property property = propertyRepository.findById(propertyId).orElse(null);
        if (property == null) {
            return List.of();
        }

        // Find properties in the same area with similar number of bedrooms
        String location = property.getLocation();
        Integer bedrooms = property.getBedrooms();

        if (location == null || bedrooms == null) {
            return List.of();
        }

        return propertyRepository.searchProperties(location, null, null, bedrooms,
                property.getPropertyType(), property.getListingType());
    }
}