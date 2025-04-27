package com.example.property_finder.controller;

import com.example.property_finder.dto.ReviewDTO;
import com.example.property_finder.model.Review;
import com.example.property_finder.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        List<ReviewDTO> reviewDTOs = convertToDTOList(reviews);
        return ResponseEntity.ok(reviewDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        Optional<Review> reviewOptional = reviewService.getReviewById(id);

        if (reviewOptional.isPresent()) {
            ReviewDTO reviewDTO = convertToDTO(reviewOptional.get());
            return ResponseEntity.ok(reviewDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByPropertyId(@PathVariable Long propertyId) {
        List<Review> reviews = reviewService.getReviewsByPropertyId(propertyId);
        List<ReviewDTO> reviewDTOs = convertToDTOList(reviews);
        return ResponseEntity.ok(reviewDTOs);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByUserId(@PathVariable Long userId) {
        List<Review> reviews = reviewService.getReviewsByUserId(userId);
        List<ReviewDTO> reviewDTOs = convertToDTOList(reviews);
        return ResponseEntity.ok(reviewDTOs);
    }

    @GetMapping("/property/{propertyId}/stats")
    public ResponseEntity<Map<String, Object>> getPropertyReviewStats(@PathVariable Long propertyId) {
        Double averageRating = reviewService.getAverageRating(propertyId);
        Long reviewCount = reviewService.getReviewCount(propertyId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("averageRating", averageRating != null ? averageRating : 0.0);
        stats.put("reviewCount", reviewCount);

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<ReviewDTO>> getRecentReviews(@RequestParam(defaultValue = "10") int limit) {
        List<Review> reviews = reviewService.getRecentReviews(limit);
        List<ReviewDTO> reviewDTOs = convertToDTOList(reviews);
        return ResponseEntity.ok(reviewDTOs);
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
        Optional<Review> createdReviewOptional = reviewService.addReview(
                reviewDTO.getUserId(),
                reviewDTO.getPropertyId(),
                reviewDTO.getRating(),
                reviewDTO.getComment()
        );

        if (createdReviewOptional.isPresent()) {
            ReviewDTO createdReviewDTO = convertToDTO(createdReviewOptional.get());
            return new ResponseEntity<>(createdReviewDTO, HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().build(); // User already reviewed or invalid data
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable Long id,
            @RequestBody ReviewDTO reviewDTO) {
        Optional<Review> updatedReviewOptional = reviewService.updateReview(
                id,
                reviewDTO.getRating(),
                reviewDTO.getComment()
        );

        if (updatedReviewOptional.isPresent()) {
            ReviewDTO updatedReviewDTO = convertToDTO(updatedReviewOptional.get());
            return ResponseEntity.ok(updatedReviewDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        boolean deleted = reviewService.deleteReview(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Helper methods for DTO conversion
    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setPropertyId(review.getProperty().getId());
        dto.setUserId(review.getUser().getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());

        // Set user name if available
        if (review.getUser() != null) {
            String userName = review.getUser().getFirstName() + " " + review.getUser().getLastName();
            dto.setUserName(userName.trim());
        }

        // Set property title if available
        if (review.getProperty() != null) {
            dto.setPropertyTitle(review.getProperty().getTitle());
        }

        return dto;
    }

    private List<ReviewDTO> convertToDTOList(List<Review> reviews) {
        List<ReviewDTO> dtos = new ArrayList<>();

        for (Review review : reviews) {
            dtos.add(convertToDTO(review));
        }

        return dtos;
    }
}