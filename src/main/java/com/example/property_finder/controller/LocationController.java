package com.example.property_finder.controller;

import com.example.property_finder.dto.LocationDTO;
import com.example.property_finder.model.Location;
import com.example.property_finder.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        List<LocationDTO> locationDTOs = convertToDTOList(locations);
        return ResponseEntity.ok(locationDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Long id) {
        Optional<Location> locationOptional = locationService.getLocationById(id);

        if (locationOptional.isPresent()) {
            LocationDTO locationDTO = convertToDTO(locationOptional.get());
            return ResponseEntity.ok(locationDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<LocationDTO>> getLocationsByType(@PathVariable String type) {
        List<Location> locations = locationService.getLocationsByType(type);
        List<LocationDTO> locationDTOs = convertToDTOList(locations);
        return ResponseEntity.ok(locationDTOs);
    }

    @GetMapping("/parent/{parentLocationId}")
    public ResponseEntity<List<LocationDTO>> getSubLocations(@PathVariable Long parentLocationId) {
        List<Location> locations = locationService.getSubLocations(parentLocationId);
        List<LocationDTO> locationDTOs = convertToDTOList(locations);
        return ResponseEntity.ok(locationDTOs);
    }

    @GetMapping("/root")
    public ResponseEntity<List<LocationDTO>> getRootLocations() {
        List<Location> locations = locationService.getRootLocations();
        List<LocationDTO> locationDTOs = convertToDTOList(locations);
        return ResponseEntity.ok(locationDTOs);
    }

    @GetMapping("/search")
    public ResponseEntity<List<LocationDTO>> searchLocations(@RequestParam String name) {
        List<Location> locations = locationService.searchLocationsByName(name);
        List<LocationDTO> locationDTOs = convertToDTOList(locations);
        return ResponseEntity.ok(locationDTOs);
    }

    @GetMapping("/city/{cityName}/neighborhoods")
    public ResponseEntity<List<LocationDTO>> getNeighborhoods(@PathVariable String cityName) {
        List<Location> locations = locationService.getNeighborhoods(cityName);
        List<LocationDTO> locationDTOs = convertToDTOList(locations);
        return ResponseEntity.ok(locationDTOs);
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<LocationDTO>> getNearbyLocations(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5.0") Double radiusInKm) {

        List<Location> locations = locationService.getNearbyLocations(latitude, longitude, radiusInKm);
        List<LocationDTO> locationDTOs = convertToDTOList(locations);
        return ResponseEntity.ok(locationDTOs);
    }

    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody LocationDTO locationDTO) {
        Location location = locationService.createLocation(
                locationDTO.getName(),
                locationDTO.getDescription(),
                locationDTO.getType(),
                locationDTO.getParentLocationId(),
                locationDTO.getLatitude(),
                locationDTO.getLongitude()
        );

        LocationDTO createdLocationDTO = convertToDTO(location);
        return new ResponseEntity<>(createdLocationDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDTO> updateLocation(
            @PathVariable Long id,
            @RequestBody LocationDTO locationDTO) {

        Optional<Location> updatedLocationOptional = locationService.updateLocation(
                id,
                locationDTO.getName(),
                locationDTO.getDescription(),
                locationDTO.getType(),
                locationDTO.getParentLocationId(),
                locationDTO.getLatitude(),
                locationDTO.getLongitude()
        );

        if (updatedLocationOptional.isPresent()) {
            LocationDTO updatedLocationDTO = convertToDTO(updatedLocationOptional.get());
            return ResponseEntity.ok(updatedLocationDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/move/{newParentId}")
    public ResponseEntity<Void> moveLocation(
            @PathVariable Long id,
            @PathVariable Long newParentId) {

        boolean moved = locationService.moveLocation(id, newParentId);

        if (moved) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        boolean deleted = locationService.deleteLocation(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Helper methods for DTO conversion
    private LocationDTO convertToDTO(Location location) {
        LocationDTO dto = new LocationDTO();
        dto.setId(location.getId());
        dto.setName(location.getName());
        dto.setDescription(location.getDescription());
        dto.setType(location.getType());

        if (location.getParentLocation() != null) {
            dto.setParentLocationId(location.getParentLocation().getId());
            dto.setParentLocationName(location.getParentLocation().getName());
        }

        // Include sub-locations if any
        if (location.getSubLocations() != null && !location.getSubLocations().isEmpty()) {
            List<LocationDTO> subLocationDTOs = new ArrayList<>();
            for (Location subLocation : location.getSubLocations()) {
                // Avoid infinite recursion by not including sub-sub-locations
                LocationDTO subDto = new LocationDTO();
                subDto.setId(subLocation.getId());
                subDto.setName(subLocation.getName());
                subDto.setType(subLocation.getType());
                subLocationDTOs.add(subDto);
            }
            dto.setSubLocations(subLocationDTOs);
        }

        dto.setLatitude(location.getLatitude());
        dto.setLongitude(location.getLongitude());
        dto.setCreatedAt(location.getCreatedAt());

        return dto;
    }

    private List<LocationDTO> convertToDTOList(List<Location> locations) {
        List<LocationDTO> dtos = new ArrayList<>();

        for (Location location : locations) {
            dtos.add(convertToDTO(location));
        }

        return dtos;
    }
}