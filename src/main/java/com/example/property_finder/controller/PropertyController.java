package com.example.property_finder.controller;

import com.example.property_finder.dto.PropertyDTO;
import com.example.property_finder.model.Property;
import com.example.property_finder.model.User;
import com.example.property_finder.service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping
    public ResponseEntity<List<PropertyDTO>> getAllProperties() {
        List<Property> properties = propertyService.getAllProperties();
        List<PropertyDTO> propertyDTOs = convertToDTOList(properties);
        return ResponseEntity.ok(propertyDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyDTO> getPropertyById(@PathVariable Long id) {
        Optional<Property> propertyOptional = propertyService.getPropertyById(id);

        if (propertyOptional.isPresent()) {
            PropertyDTO propertyDTO = convertToDTO(propertyOptional.get());
            return ResponseEntity.ok(propertyDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<PropertyDTO>> searchProperties(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer bedrooms,
            @RequestParam(required = false) String propertyType,
            @RequestParam(required = false) String listingType) {

        List<Property> properties = propertyService.searchProperties(
                location, minPrice, maxPrice, bedrooms, propertyType, listingType);

        List<PropertyDTO> propertyDTOs = convertToDTOList(properties);
        return ResponseEntity.ok(propertyDTOs);
    }

    @GetMapping("/byOwner/{ownerId}")
    public ResponseEntity<List<PropertyDTO>> getPropertiesByOwner(@PathVariable Long ownerId) {
        List<Property> properties = propertyService.getPropertiesByOwner(ownerId);
        List<PropertyDTO> propertyDTOs = convertToDTOList(properties);
        return ResponseEntity.ok(propertyDTOs);
    }

    @PostMapping
    public ResponseEntity<PropertyDTO> createProperty(
            @RequestBody PropertyDTO propertyDTO) {

        Property property = convertToEntity(propertyDTO);
        Property savedProperty = propertyService.saveProperty(property, propertyDTO.getOwnerId());

        return new ResponseEntity<>(convertToDTO(savedProperty), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyDTO> updateProperty(
            @PathVariable Long id,
            @RequestBody PropertyDTO propertyDTO) {

        Property property = convertToEntity(propertyDTO);
        Optional<Property> updatedPropertyOptional = propertyService.updateProperty(id, property);

        if (updatedPropertyOptional.isPresent()) {
            PropertyDTO updatedPropertyDTO = convertToDTO(updatedPropertyOptional.get());
            return ResponseEntity.ok(updatedPropertyDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        boolean deleted = propertyService.deleteProperty(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
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

    private Property convertToEntity(PropertyDTO dto) {
        Property property = new Property();
        property.setId(dto.getId());
        property.setTitle(dto.getTitle());
        property.setDescription(dto.getDescription());
        property.setPrice(dto.getPrice());
        property.setLocation(dto.getLocation());
        property.setBedrooms(dto.getBedrooms());
        property.setBathrooms(dto.getBathrooms());
        property.setArea(dto.getArea());
        property.setPropertyType(dto.getPropertyType());
        property.setListingType(dto.getListingType());

        // Owner is set in the service layer

        return property;
    }

    private List<PropertyDTO> convertToDTOList(List<Property> properties) {
        List<PropertyDTO> dtos = new ArrayList<>();

        for (Property property : properties) {
            dtos.add(convertToDTO(property));
        }

        return dtos;
    }
}