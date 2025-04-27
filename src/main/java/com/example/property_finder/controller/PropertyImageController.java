package com.example.property_finder.controller;

import com.example.property_finder.dto.PropertyImageDTO;
import com.example.property_finder.model.PropertyImage;
import com.example.property_finder.service.PropertyImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/property-images")
public class PropertyImageController {

    private final PropertyImageService propertyImageService;

    public PropertyImageController(PropertyImageService propertyImageService) {
        this.propertyImageService = propertyImageService;
    }

    @GetMapping
    public ResponseEntity<List<PropertyImageDTO>> getAllImages() {
        List<PropertyImage> images = propertyImageService.getAllImages();
        List<PropertyImageDTO> imageDTOs = convertToDTOList(images);
        return ResponseEntity.ok(imageDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyImageDTO> getImageById(@PathVariable Long id) {
        Optional<PropertyImage> imageOptional = propertyImageService.getImageById(id);

        if (imageOptional.isPresent()) {
            PropertyImageDTO imageDTO = convertToDTO(imageOptional.get());
            return ResponseEntity.ok(imageDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<PropertyImageDTO>> getImagesByPropertyId(@PathVariable Long propertyId) {
        List<PropertyImage> images = propertyImageService.getImagesByPropertyId(propertyId);
        List<PropertyImageDTO> imageDTOs = convertToDTOList(images);
        return ResponseEntity.ok(imageDTOs);
    }

    @GetMapping("/property/{propertyId}/primary")
    public ResponseEntity<PropertyImageDTO> getPrimaryImageByPropertyId(@PathVariable Long propertyId) {
        Optional<PropertyImage> imageOptional = propertyImageService.getPrimaryImageByPropertyId(propertyId);

        if (imageOptional.isPresent()) {
            PropertyImageDTO imageDTO = convertToDTO(imageOptional.get());
            return ResponseEntity.ok(imageDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/property/{propertyId}/non-primary")
    public ResponseEntity<List<PropertyImageDTO>> getNonPrimaryImagesByPropertyId(@PathVariable Long propertyId) {
        List<PropertyImage> images = propertyImageService.getNonPrimaryImagesByPropertyId(propertyId);
        List<PropertyImageDTO> imageDTOs = convertToDTOList(images);
        return ResponseEntity.ok(imageDTOs);
    }

    @PostMapping
    public ResponseEntity<PropertyImageDTO> createImage(@RequestBody PropertyImageDTO imageDTO) {
        Optional<PropertyImage> createdImageOptional = propertyImageService.addImage(
                imageDTO.getPropertyId(),
                imageDTO.getImageUrl(),
                imageDTO.getDescription(),
                imageDTO.getIsPrimary()
        );

        if (createdImageOptional.isPresent()) {
            PropertyImageDTO createdImageDTO = convertToDTO(createdImageOptional.get());
            return new ResponseEntity<>(createdImageDTO, HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyImageDTO> updateImage(
            @PathVariable Long id,
            @RequestBody PropertyImageDTO imageDTO) {
        Optional<PropertyImage> updatedImageOptional = propertyImageService.updateImage(
                id,
                imageDTO.getImageUrl(),
                imageDTO.getDescription(),
                imageDTO.getIsPrimary()
        );

        if (updatedImageOptional.isPresent()) {
            PropertyImageDTO updatedImageDTO = convertToDTO(updatedImageOptional.get());
            return ResponseEntity.ok(updatedImageDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/set-primary")
    public ResponseEntity<Void> setPrimaryImage(@PathVariable Long id) {
        boolean success = propertyImageService.setPrimaryImage(id);

        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        boolean deleted = propertyImageService.deleteImage(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/property/{propertyId}")
    public ResponseEntity<Void> deleteImagesByPropertyId(@PathVariable Long propertyId) {
        propertyImageService.deleteImagesByPropertyId(propertyId);
        return ResponseEntity.noContent().build();
    }

    // Helper methods for DTO conversion
    private PropertyImageDTO convertToDTO(PropertyImage image) {
        PropertyImageDTO dto = new PropertyImageDTO();
        dto.setId(image.getId());
        dto.setPropertyId(image.getProperty().getId());
        dto.setImageUrl(image.getImageUrl());
        dto.setDescription(image.getDescription());
        dto.setIsPrimary(image.getIsPrimary());
        dto.setCreatedAt(image.getCreatedAt());

        return dto;
    }

    private List<PropertyImageDTO> convertToDTOList(List<PropertyImage> images) {
        List<PropertyImageDTO> dtos = new ArrayList<>();

        for (PropertyImage image : images) {
            dtos.add(convertToDTO(image));
        }

        return dtos;
    }
}