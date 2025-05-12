package com.example.property_finder.service;

import com.example.property_finder.model.Location;
import com.example.property_finder.repository.LocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    public List<Location> getLocationsByType(String type) {
        return locationRepository.findByType(type);
    }

    public List<Location> getSubLocations(Long parentLocationId) {
        return locationRepository.findByParentLocationId(parentLocationId);
    }

    public List<Location> getRootLocations() {
        return locationRepository.findRootLocations();
    }

    public List<Location> searchLocationsByName(String name) {
        return locationRepository.findByNameContaining(name);
    }

    public List<Location> getNeighborhoods(String cityName) {
        return locationRepository.findNeighborhoods(cityName);
    }

    public List<Location> getNearbyLocations(Double latitude, Double longitude, Double radiusInKm) {
        return locationRepository.findNearbyLocations(latitude, longitude, radiusInKm);
    }

    @Transactional
    public Location createLocation(String name, String description, String type,
                                   Long parentLocationId, Double latitude, Double longitude) {
        Location location = new Location();
        location.setName(name);
        location.setDescription(description);
        location.setType(type);

        if (parentLocationId != null) {
            locationRepository.findById(parentLocationId).ifPresent(location::setParentLocation);
        }

        location.setLatitude(latitude);
        location.setLongitude(longitude);

        return locationRepository.save(location);
    }

    @Transactional
    public Optional<Location> updateLocation(Long id, String name, String description,
                                             String type, Long parentLocationId,
                                             Double latitude, Double longitude) {
        Optional<Location> locationOptional = locationRepository.findById(id);

        if (locationOptional.isPresent()) {
            Location location = locationOptional.get();

            if (name != null) {
                location.setName(name);
            }
            if (description != null) {
                location.setDescription(description);
            }
            if (type != null) {
                location.setType(type);
            }

            // Update parent location if provided, otherwise keep existing parent
            if (parentLocationId != null) {
                // Prevent circular references
                if (!id.equals(parentLocationId)) {
                    locationRepository.findById(parentLocationId)
                            .ifPresent(location::setParentLocation);
                }
            }

            if (latitude != null) {
                location.setLatitude(latitude);
            }
            if (longitude != null) {
                location.setLongitude(longitude);
            }

            Location updatedLocation = locationRepository.save(location);
            return Optional.of(updatedLocation);
        }

        return Optional.empty();
    }

    @Transactional
    public boolean deleteLocation(Long id) {
        if (locationRepository.findById(id).isPresent()) {
            locationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean moveLocation(Long locationId, Long newParentId) {
        Optional<Location> locationOptional = locationRepository.findById(locationId);
        Optional<Location> newParentOptional = locationRepository.findById(newParentId);

        if (locationOptional.isPresent() && newParentOptional.isPresent()) {
            Location location = locationOptional.get();
            Location newParent = newParentOptional.get();

            // Prevent circular references
            if (isAncestor(location, newParent)) {
                return false;
            }

            location.setParentLocation(newParent);
            locationRepository.save(location);
            return true;
        }

        return false;
    }

    // Helper method to check if potentialAncestor is an ancestor of location
    private boolean isAncestor(Location location, Location potentialAncestor) {
        if (location == null || potentialAncestor == null) {
            return false;
        }

        // If the location's ID matches the potential ancestor's ID, it would create a cycle
        if (location.getId().equals(potentialAncestor.getId())) {
            return true;
        }

        // Check up the hierarchy
        Location parent = potentialAncestor.getParentLocation();
        while (parent != null) {
            if (parent.getId().equals(location.getId())) {
                return true;
            }
            parent = parent.getParentLocation();
        }

        return false;
    }
}