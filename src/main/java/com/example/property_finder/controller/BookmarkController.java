package com.example.property_finder.controller;

import com.example.property_finder.dto.BookmarkDTO;
import com.example.property_finder.dto.PropertyDTO;
import com.example.property_finder.model.Bookmark;
import com.example.property_finder.model.Property;
import com.example.property_finder.model.User;
import com.example.property_finder.service.BookmarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @GetMapping
    public ResponseEntity<List<BookmarkDTO>> getAllBookmarks() {
        List<Bookmark> bookmarks = bookmarkService.getAllBookmarks();
        List<BookmarkDTO> bookmarkDTOs = convertToDTOList(bookmarks);
        return ResponseEntity.ok(bookmarkDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookmarkDTO> getBookmarkById(@PathVariable Long id) {
        Optional<Bookmark> bookmarkOptional = bookmarkService.getBookmarkById(id);

        if (bookmarkOptional.isPresent()) {
            BookmarkDTO bookmarkDTO = convertToDTO(bookmarkOptional.get());
            return ResponseEntity.ok(bookmarkDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookmarkDTO>> getUserBookmarks(@PathVariable Long userId) {
        List<Bookmark> bookmarks = bookmarkService.getBookmarksByUserId(userId);
        List<BookmarkDTO> bookmarkDTOs = convertToDTOList(bookmarks);
        return ResponseEntity.ok(bookmarkDTOs);
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<BookmarkDTO>> getPropertyBookmarks(@PathVariable Long propertyId) {
        List<Bookmark> bookmarks = bookmarkService.getBookmarksByPropertyId(propertyId);
        List<BookmarkDTO> bookmarkDTOs = convertToDTOList(bookmarks);
        return ResponseEntity.ok(bookmarkDTOs);
    }

    @GetMapping("/check/{userId}/{propertyId}")
    public ResponseEntity<Boolean> isPropertyBookmarked(
            @PathVariable Long userId,
            @PathVariable Long propertyId) {
        boolean isBookmarked = bookmarkService.isPropertyBookmarkedByUser(userId, propertyId);
        return ResponseEntity.ok(isBookmarked);
    }

    @PostMapping
    public ResponseEntity<BookmarkDTO> createBookmark(@RequestBody BookmarkDTO bookmarkDTO) {
        Optional<Bookmark> createdBookmarkOptional = bookmarkService.addBookmark(
                bookmarkDTO.getUserId(),
                bookmarkDTO.getPropertyId());

        if (createdBookmarkOptional.isPresent()) {
            BookmarkDTO createdBookmarkDTO = convertToDTO(createdBookmarkOptional.get());
            return new ResponseEntity<>(createdBookmarkDTO, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Already exists or invalid data
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookmarkById(@PathVariable Long id) {
        boolean deleted = bookmarkService.removeBookmarkById(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/user/{userId}/property/{propertyId}")
    public ResponseEntity<Void> deleteBookmarkByUserAndProperty(
            @PathVariable Long userId,
            @PathVariable Long propertyId) {
        boolean deleted = bookmarkService.removeBookmark(userId, propertyId);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Helper methods for DTO conversion
    private BookmarkDTO convertToDTO(Bookmark bookmark) {
        BookmarkDTO dto = new BookmarkDTO();
        dto.setId(bookmark.getId());
        dto.setUserId(bookmark.getUser().getId());
        dto.setPropertyId(bookmark.getProperty().getId());
        dto.setCreatedAt(bookmark.getCreatedAt());

        // Include property details in the response
        Property property = bookmark.getProperty();
        PropertyDTO propertyDTO = convertPropertyToDTO(property);
        dto.setProperty(propertyDTO);

        return dto;
    }

    private PropertyDTO convertPropertyToDTO(Property property) {
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

    private List<BookmarkDTO> convertToDTOList(List<Bookmark> bookmarks) {
        List<BookmarkDTO> dtos = new ArrayList<>();

        for (Bookmark bookmark : bookmarks) {
            dtos.add(convertToDTO(bookmark));
        }

        return dtos;
    }
}