package com.example.property_finder.repository;

import com.example.property_finder.model.Location;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LocationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Location> findAll() {
        return entityManager.createQuery("SELECT l FROM Location l", Location.class).getResultList();
    }

    public Optional<Location> findById(Long id) {
        Location location = entityManager.find(Location.class, id);
        return Optional.ofNullable(location);
    }

    public List<Location> findByType(String type) {
        TypedQuery<Location> query = entityManager.createQuery(
                "SELECT l FROM Location l WHERE l.type = :type", Location.class);
        query.setParameter("type", type);
        return query.getResultList();
    }

    public List<Location> findByParentLocationId(Long parentLocationId) {
        TypedQuery<Location> query = entityManager.createQuery(
                "SELECT l FROM Location l WHERE l.parentLocation.id = :parentLocationId", Location.class);
        query.setParameter("parentLocationId", parentLocationId);
        return query.getResultList();
    }

    public List<Location> findRootLocations() {
        TypedQuery<Location> query = entityManager.createQuery(
                "SELECT l FROM Location l WHERE l.parentLocation IS NULL", Location.class);
        return query.getResultList();
    }

    public List<Location> findByNameContaining(String name) {
        TypedQuery<Location> query = entityManager.createQuery(
                "SELECT l FROM Location l WHERE LOWER(l.name) LIKE LOWER(:name)", Location.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }

    public List<Location> findNeighborhoods(String cityName) {
        TypedQuery<Location> query = entityManager.createQuery(
                "SELECT l FROM Location l WHERE l.parentLocation.name = :cityName AND l.type = 'Neighborhood'",
                Location.class);
        query.setParameter("cityName", cityName);
        return query.getResultList();
    }

    public List<Location> findNearbyLocations(Double latitude, Double longitude, Double radiusInKm) {
        // Haversine formula for distance calculation
        String queryString = "SELECT l FROM Location l WHERE " +
                "(6371 * acos(cos(radians(:latitude)) * cos(radians(l.latitude)) * " +
                "cos(radians(l.longitude) - radians(:longitude)) + " +
                "sin(radians(:latitude)) * sin(radians(l.latitude)))) <= :radius";

        TypedQuery<Location> query = entityManager.createQuery(queryString, Location.class);
        query.setParameter("latitude", latitude);
        query.setParameter("longitude", longitude);
        query.setParameter("radius", radiusInKm);

        return query.getResultList();
    }

    @Transactional
    public Location save(Location location) {
        if (location.getId() == null) {
            entityManager.persist(location);
            return location;
        } else {
            return entityManager.merge(location);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        Location location = entityManager.find(Location.class, id);
        if (location != null) {
            entityManager.remove(location);
        }
    }
}