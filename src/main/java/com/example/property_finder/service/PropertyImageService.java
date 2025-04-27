package com.example.property_finder.service;

import com.example.property_finder.model.Property;
import com.example.property_finder.model.PropertyImage;
import com.example.property_finder.repository.PropertyImageRepository;
import com.example.property_finder.repository.PropertyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyImageService {

    private final PropertyImageRepository propertyImageRepository;
    private final PropertyRepository propertyRepository;

    public PropertyImageService(PropertyImageRepository propertyImageRepository,
                                PropertyRepository propertyRepository) {
        this.propertyImageRepository = propertyImageRepository;
        this.propertyRepository = propertyRepository;
    }

    public List<PropertyImage> getAllImages() {
        return propertyImageRepository.findAll();
    }

    public Optional<PropertyImage> getImageById(Long id) {
        return propertyImageRepository.findById(id);
    }

    public List<PropertyImage> getImagesByPropertyId(Long propertyId) {
        return propertyImageRepository.findByPropertyId(propertyId);
    }

    public Optional<PropertyImage> getPrimaryImageByPropertyId(Long propertyId) {
        return propertyImageRepository.findPrimaryImageByPropertyId(propertyId);
    }

    public List<PropertyImage> getNonPrimaryImagesByPropertyId(Long propertyId) {
        return propertyImageRepository.findNonPrimaryImagesByPropertyId(propertyId);
    }

    @Transactional
    public Optional<PropertyImage> addImage(Long propertyId, String imageUrl, String description, Boolean isPrimary) {
        Optional<Property> propertyOptional = propertyRepository.findById(propertyId);

        if (propertyOptional.isPresent()) {
            Property property = propertyOptional.get();

            // If this is meant to be the primary image, first unset any existing primary image
            if (isPrimary != null && isPrimary) {
                List<PropertyImage> existingImages = propertyImageRepository.findByPropertyId(propertyId);
                for (PropertyImage existingImage : existingImages) {
                    if (existingImage.getIsPrimary()) {
                        existingImage.setIsPrimary(false);
                        propertyImageRepository.save(existingImage);
                    }
                }
            }

            PropertyImage image = new PropertyImage(property, imageUrl, description, isPrimary);
            PropertyImage savedImage = propertyImageRepository.save(image);
            return Optional.of(savedImage);
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<PropertyImage> updateImage(Long id, String imageUrl, String description, Boolean isPrimary) {
        Optional<PropertyImage> imageOptional = propertyImageRepository.findById(id);

        if (imageOptional.isPresent()) {
            PropertyImage image = imageOptional.get();

            if (imageUrl != null) {
                image.setImageUrl(imageUrl);
            }
            if (description != null) {
                image.setDescription(description);
            }
            if (isPrimary != null) {
                // If setting as primary, unset other primary images for the same property
                if (isPrimary) {
                    List<PropertyImage> existingImages = propertyImageRepository.findByPropertyId(image.getProperty().getId());
                    for (PropertyImage existingImage : existingImages) {
                        if (!existingImage.getId().equals(id) && existingImage.getIsPrimary()) {
                            existingImage.setIsPrimary(false);
                            propertyImageRepository.save(existingImage);
                        }
                    }
                }
                image.setIsPrimary(isPrimary);
            }

            PropertyImage updatedImage = propertyImageRepository.save(image);
            return Optional.of(updatedImage);
        }

        return Optional.empty();
    }

    @Transactional
    public boolean deleteImage(Long id) {
        if (propertyImageRepository.findById(id).isPresent()) {
            propertyImageRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public void deleteImagesByPropertyId(Long propertyId) {
        propertyImageRepository.deleteByPropertyId(propertyId);
    }

    @Transactional
    public boolean setPrimaryImage(Long imageId) {
        Optional<PropertyImage> imageOptional = propertyImageRepository.findById(imageId);

        if (imageOptional.isPresent()) {
            propertyImageRepository.setPrimaryImage(imageId);
            return true;
        }

        return false;
    }
}