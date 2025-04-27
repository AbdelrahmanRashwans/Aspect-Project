package com.example.property_finder.service;

import com.example.property_finder.model.Property;
import com.example.property_finder.model.Review;
import com.example.property_finder.model.User;
import com.example.property_finder.repository.PropertyRepository;
import com.example.property_finder.repository.ReviewRepository;
import com.example.property_finder.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         UserRepository userRepository,
                         PropertyRepository propertyRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    public List<Review> getReviewsByPropertyId(Long propertyId) {
        return reviewRepository.findByPropertyId(propertyId);
    }

    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    public Optional<Review> getReviewByUserAndProperty(Long userId, Long propertyId) {
        return reviewRepository.findByUserIdAndPropertyId(userId, propertyId);
    }

    public Double getAverageRating(Long propertyId) {
        return reviewRepository.getAverageRatingByPropertyId(propertyId);
    }

    public Long getReviewCount(Long propertyId) {
        return reviewRepository.getReviewCountByPropertyId(propertyId);
    }

    public List<Review> getRecentReviews(int limit) {
        return reviewRepository.findRecentReviews(limit);
    }

    @Transactional
    public Optional<Review> addReview(Long userId, Long propertyId, Integer rating, String comment) {
        // Check if the user has already reviewed this property
        if (reviewRepository.findByUserIdAndPropertyId(userId, propertyId).isPresent()) {
            return Optional.empty(); // User has already reviewed this property
        }

        // Validate rating is between 1 and 5
        if (rating < 1 || rating > 5) {
            return Optional.empty();
        }

        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Property> propertyOptional = propertyRepository.findById(propertyId);

        if (userOptional.isPresent() && propertyOptional.isPresent()) {
            User user = userOptional.get();
            Property property = propertyOptional.get();

            Review review = new Review(property, user, rating, comment);
            Review savedReview = reviewRepository.save(review);
            return Optional.of(savedReview);
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<Review> updateReview(Long reviewId, Integer rating, String comment) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);

        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();

            // Validate rating if provided
            if (rating != null) {
                if (rating < 1 || rating > 5) {
                    return Optional.empty();
                }
                review.setRating(rating);
            }

            if (comment != null) {
                review.setComment(comment);
            }

            Review updatedReview = reviewRepository.save(review);
            return Optional.of(updatedReview);
        }

        return Optional.empty();
    }

    @Transactional
    public boolean deleteReview(Long id) {
        if (reviewRepository.findById(id).isPresent()) {
            reviewRepository.deleteById(id);
            return true;
        }
        return false;
    }
}