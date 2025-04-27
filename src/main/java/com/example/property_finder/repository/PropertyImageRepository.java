package com.example.property_finder.repository;

import com.example.property_finder.model.PropertyImage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PropertyImageRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<PropertyImage> findAll() {
        return entityManager.createQuery("SELECT pi FROM PropertyImage pi", PropertyImage.class).getResultList();
    }

    public Optional<PropertyImage> findById(Long id) {
        PropertyImage image = entityManager.find(PropertyImage.class, id);
        return Optional.ofNullable(image);
    }

    public List<PropertyImage> findByPropertyId(Long propertyId) {
        TypedQuery<PropertyImage> query = entityManager.createQuery(
                "SELECT pi FROM PropertyImage pi WHERE pi.property.id = :propertyId ORDER BY pi.isPrimary DESC, pi.createdAt ASC",
                PropertyImage.class);
        query.setParameter("propertyId", propertyId);
        return query.getResultList();
    }

    public Optional<PropertyImage> findPrimaryImageByPropertyId(Long propertyId) {
        try {
            TypedQuery<PropertyImage> query = entityManager.createQuery(
                    "SELECT pi FROM PropertyImage pi WHERE pi.property.id = :propertyId AND pi.isPrimary = true",
                    PropertyImage.class);
            query.setParameter("propertyId", propertyId);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<PropertyImage> findNonPrimaryImagesByPropertyId(Long propertyId) {
        TypedQuery<PropertyImage> query = entityManager.createQuery(
                "SELECT pi FROM PropertyImage pi WHERE pi.property.id = :propertyId AND pi.isPrimary = false ORDER BY pi.createdAt ASC",
                PropertyImage.class);
        query.setParameter("propertyId", propertyId);
        return query.getResultList();
    }

    @Transactional
    public PropertyImage save(PropertyImage image) {
        if (image.getId() == null) {
            entityManager.persist(image);
            return image;
        } else {
            return entityManager.merge(image);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        PropertyImage image = entityManager.find(PropertyImage.class, id);
        if (image != null) {
            entityManager.remove(image);
        }
    }

    @Transactional
    public void deleteByPropertyId(Long propertyId) {
        TypedQuery<PropertyImage> query = entityManager.createQuery(
                "SELECT pi FROM PropertyImage pi WHERE pi.property.id = :propertyId",
                PropertyImage.class);
        query.setParameter("propertyId", propertyId);

        List<PropertyImage> images = query.getResultList();
        for (PropertyImage image : images) {
            entityManager.remove(image);
        }
    }

    @Transactional
    public void setPrimaryImage(Long imageId) {
        // First get the image to find the property
        PropertyImage image = entityManager.find(PropertyImage.class, imageId);
        if (image != null) {
            // Reset all images for this property to non-primary
            TypedQuery<PropertyImage> query = entityManager.createQuery(
                    "UPDATE PropertyImage pi SET pi.isPrimary = false WHERE pi.property.id = :propertyId",
                    PropertyImage.class);
            query.setParameter("propertyId", image.getProperty().getId());
            query.executeUpdate();

            // Set the specified image as primary
            image.setIsPrimary(true);
            entityManager.merge(image);
        }
    }
}