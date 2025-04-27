package com.example.property_finder.controller;

import com.example.property_finder.dto.PropertyDTO;
import com.example.property_finder.dto.PropertySearchDTO;
import com.example.property_finder.model.Property;
import com.example.property_finder.model.User;
import com.example.property_finder.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping
    public ResponseEntity<List<PropertyDTO>> searchProperties(@RequestBody PropertySearchDTO searchDTO) {
        List<Property> properties = searchService.searchProperties(searchDTO);
        List<PropertyDTO> propertyDTOs = convertToDTOList(properties);
        return ResponseEntity.ok(propertyDTOs);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<PropertyDTO>> getFeaturedProperties() {
        List<Property> properties = searchService.findFeaturedProperties();
        List<PropertyDTO> propertyDTOs = convertToDTOList(properties);
        return ResponseEntity.ok(propertyDTOs);
    }

    @GetMapping("/similar/{propertyId}")
    public ResponseEntity<List<PropertyDTO>> getSimilarProperties(@PathVariable Long propertyId) {
        List<Property> properties = searchService.findSimilarProperties(propertyId);
        List<PropertyDTO> propertyDTOs = convertToDTOList(properties);
        return ResponseEntity.ok(propertyDTOs);
    }

    // Helper methods for DTO conversion
    private PropertyDTO convertToDTO(Property property) {
        PropertyDTO dto = new PropertyDTO();
        dto.setId(property.getId());
        dto.setTitle(property.getTitle());
        dto.setDescription(property.getDescription());
        dto.setPrice(property.getPrice());
        dto.setLocation(property.getLocation());
        dto.setBedrooms(property.getBedrooms());
        dto.setBathrooms(property.getBathrooms());
        dto.setArea(property.getArea());
        dto.setPropertyType(property.getPropertyType());
        dto.setListingType(property.getListingType());

        // Set owner information if available
        User owner = property.getOwner();
        if (owner != null) {
            dto.setOwnerId(owner.getId());
            String ownerName = owner.getFirstName() + " " + owner.getLastName();
            dto.setOwnerName(ownerName.trim());
        }

        return dto;
    }

    private List<PropertyDTO> convertToDTOList(List<Property> properties) {
        List<PropertyDTO> dtos = new ArrayList<>();

        for (Property property : properties) {
            dtos.add(convertToDTO(property));
        }

        return dtos;
    }
}