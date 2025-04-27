package com.example.property_finder.repository;

import com.example.property_finder.dto.PropertySearchDTO;
import com.example.property_finder.model.Property;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class PropertyRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Property> findAll() {
        return entityManager.createQuery("SELECT p FROM Property p", Property.class).getResultList();
    }

    public Optional<Property> findById(Long id) {
        Property property = entityManager.find(Property.class, id);
        return Optional.ofNullable(property);
    }

    public List<Property> findByLocation(String location) {
        TypedQuery<Property> query = entityManager.createQuery(
                "SELECT p FROM Property p WHERE p.location LIKE :location", Property.class);
        query.setParameter("location", "%" + location + "%");
        return query.getResultList();
    }

    public List<Property> findByPropertyType(String propertyType) {
        TypedQuery<Property> query = entityManager.createQuery(
                "SELECT p FROM Property p WHERE p.propertyType = :propertyType", Property.class);
        query.setParameter("propertyType", propertyType);
        return query.getResultList();
    }

    public List<Property> findByListingType(String listingType) {
        TypedQuery<Property> query = entityManager.createQuery(
                "SELECT p FROM Property p WHERE p.listingType = :listingType", Property.class);
        query.setParameter("listingType", listingType);
        return query.getResultList();
    }

    public List<Property> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        TypedQuery<Property> query = entityManager.createQuery(
                "SELECT p FROM Property p WHERE p.price BETWEEN :minPrice AND :maxPrice", Property.class);
        query.setParameter("minPrice", minPrice);
        query.setParameter("maxPrice", maxPrice);
        return query.getResultList();
    }

    public List<Property> findByBedroomsGreaterThanEqual(Integer bedrooms) {
        TypedQuery<Property> query = entityManager.createQuery(
                "SELECT p FROM Property p WHERE p.bedrooms >= :bedrooms", Property.class);
        query.setParameter("bedrooms", bedrooms);
        return query.getResultList();
    }

    public List<Property> findByOwnerId(Long ownerId) {
        TypedQuery<Property> query = entityManager.createQuery(
                "SELECT p FROM Property p WHERE p.owner.id = :ownerId", Property.class);
        query.setParameter("ownerId", ownerId);
        return query.getResultList();
    }

    public List<Property> searchProperties(String location, BigDecimal minPrice, BigDecimal maxPrice,
                                           Integer bedrooms, String propertyType, String listingType) {

        StringBuilder queryString = new StringBuilder("SELECT p FROM Property p WHERE 1=1");

        if (location != null && !location.isEmpty()) {
            queryString.append(" AND p.location LIKE :location");
        }
        if (minPrice != null) {
            queryString.append(" AND p.price >= :minPrice");
        }
        if (maxPrice != null) {
            queryString.append(" AND p.price <= :maxPrice");
        }
        if (bedrooms != null) {
            queryString.append(" AND p.bedrooms >= :bedrooms");
        }
        if (propertyType != null && !propertyType.isEmpty()) {
            queryString.append(" AND p.propertyType = :propertyType");
        }
        if (listingType != null && !listingType.isEmpty()) {
            queryString.append(" AND p.listingType = :listingType");
        }

        TypedQuery<Property> query = entityManager.createQuery(queryString.toString(), Property.class);

        if (location != null && !location.isEmpty()) {
            query.setParameter("location", "%" + location + "%");
        }
        if (minPrice != null) {
            query.setParameter("minPrice", minPrice);
        }
        if (maxPrice != null) {
            query.setParameter("maxPrice", maxPrice);
        }
        if (bedrooms != null) {
            query.setParameter("bedrooms", bedrooms);
        }
        if (propertyType != null && !propertyType.isEmpty()) {
            query.setParameter("propertyType", propertyType);
        }
        if (listingType != null && !listingType.isEmpty()) {
            query.setParameter("listingType", listingType);
        }

        return query.getResultList();
    }

    public List<Property> advancedSearch(PropertySearchDTO searchDTO) {
        StringBuilder queryString = new StringBuilder("SELECT p FROM Property p WHERE 1=1");

        if (searchDTO.getLocation() != null && !searchDTO.getLocation().isEmpty()) {
            queryString.append(" AND p.location LIKE :location");
        }
        if (searchDTO.getMinPrice() != null) {
            queryString.append(" AND p.price >= :minPrice");
        }
        if (searchDTO.getMaxPrice() != null) {
            queryString.append(" AND p.price <= :maxPrice");
        }
        if (searchDTO.getMinBedrooms() != null) {
            queryString.append(" AND p.bedrooms >= :minBedrooms");
        }
        if (searchDTO.getMinBathrooms() != null) {
            queryString.append(" AND p.bathrooms >= :minBathrooms");
        }
        if (searchDTO.getMinArea() != null) {
            queryString.append(" AND p.area >= :minArea");
        }
        if (searchDTO.getMaxArea() != null) {
            queryString.append(" AND p.area <= :maxArea");
        }
        if (searchDTO.getPropertyType() != null && !searchDTO.getPropertyType().isEmpty()) {
            queryString.append(" AND p.propertyType = :propertyType");
        }
        if (searchDTO.getListingType() != null && !searchDTO.getListingType().isEmpty()) {
            queryString.append(" AND p.listingType = :listingType");
        }

        TypedQuery<Property> query = entityManager.createQuery(queryString.toString(), Property.class);

        if (searchDTO.getLocation() != null && !searchDTO.getLocation().isEmpty()) {
            query.setParameter("location", "%" + searchDTO.getLocation() + "%");
        }
        if (searchDTO.getMinPrice() != null) {
            query.setParameter("minPrice", searchDTO.getMinPrice());
        }
        if (searchDTO.getMaxPrice() != null) {
            query.setParameter("maxPrice", searchDTO.getMaxPrice());
        }
        if (searchDTO.getMinBedrooms() != null) {
            query.setParameter("minBedrooms", searchDTO.getMinBedrooms());
        }
        if (searchDTO.getMinBathrooms() != null) {
            query.setParameter("minBathrooms", searchDTO.getMinBathrooms());
        }
        if (searchDTO.getMinArea() != null) {
            query.setParameter("minArea", searchDTO.getMinArea());
        }
        if (searchDTO.getMaxArea() != null) {
            query.setParameter("maxArea", searchDTO.getMaxArea());
        }
        if (searchDTO.getPropertyType() != null && !searchDTO.getPropertyType().isEmpty()) {
            query.setParameter("propertyType", searchDTO.getPropertyType());
        }
        if (searchDTO.getListingType() != null && !searchDTO.getListingType().isEmpty()) {
            query.setParameter("listingType", searchDTO.getListingType());
        }

        return query.getResultList();
    }

    @Transactional
    public Property save(Property property) {
        if (property.getId() == null) {
            entityManager.persist(property);
            return property;
        } else {
            return entityManager.merge(property);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        Property property = entityManager.find(Property.class, id);
        if (property != null) {
            entityManager.remove(property);
        }
    }
}