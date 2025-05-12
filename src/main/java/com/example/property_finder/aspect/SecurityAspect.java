package com.example.property_finder.aspect;

import com.example.property_finder.exception.AccessDeniedException;
import com.example.property_finder.model.Property;
import com.example.property_finder.model.Review;
import com.example.property_finder.model.User;
import com.example.property_finder.service.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
public class SecurityAspect {

    private final Logger logger = LoggerFactory.getLogger(SecurityAspect.class);

    @Autowired
    private UserService userService;

    /**
     * Pointcut for property update methods
     */
    @Pointcut("execution(* com.example.property_finder.service.PropertyService.update*(..))")
    public void propertyUpdatePointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Pointcut for property deletion methods
     */
    @Pointcut("execution(* com.example.property_finder.service.PropertyService.delete*(..))")
    public void propertyDeletePointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Pointcut for review update methods
     */
    @Pointcut("execution(* com.example.property_finder.service.ReviewService.update*(..))")
    public void reviewUpdatePointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Pointcut for review deletion methods
     */
    @Pointcut("execution(* com.example.property_finder.service.ReviewService.delete*(..))")
    public void reviewDeletePointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Security check before updating properties
     * Only property owner or admin can update properties
     */
    @Before("propertyUpdatePointcut() && args(id,..)")
    public void checkPropertyUpdateAuthorization(JoinPoint joinPoint, Long id) {
        logger.debug("Checking authorization for property update, ID: {}", id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("Not authenticated");
        }

        String username = authentication.getName();
        Optional<User> currentUser = userService.getUserByEmail(username);

        if (currentUser.isEmpty()) {
            logger.warn("User not found for username: {}", username);
            throw new AccessDeniedException("User not found");
        }

        User user = currentUser.get();

        // Admin can update any property
        if (user.getRole() == User.UserRole.ADMIN) {
            logger.debug("Admin access granted for property update");
            return;
        }

        // Check if user is the property owner
        Property property = getPropertyById(id);
        if (property == null || property.getOwner() == null ||
                !property.getOwner().getId().equals(user.getId())) {
            logger.warn("Access denied: User {} attempted to update property {}", user.getId(), id);
            throw new AccessDeniedException("You can only update your own properties");
        }

        logger.debug("Access granted for property update");
    }

    /**
     * Security check before deleting properties
     * Only property owner or admin can delete properties
     */
    @Before("propertyDeletePointcut() && args(id)")
    public void checkPropertyDeleteAuthorization(Long id) {
        logger.debug("Checking authorization for property deletion, ID: {}", id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("Not authenticated");
        }

        String username = authentication.getName();
        Optional<User> currentUser = userService.getUserByEmail(username);

        if (currentUser.isEmpty()) {
            logger.warn("User not found for username: {}", username);
            throw new AccessDeniedException("User not found");
        }

        User user = currentUser.get();

        // Admin can delete any property
        if (user.getRole() == User.UserRole.ADMIN) {
            logger.debug("Admin access granted for property deletion");
            return;
        }

        // Check if user is the property owner
        Property property = getPropertyById(id);
        if (property == null || property.getOwner() == null ||
                !property.getOwner().getId().equals(user.getId())) {
            logger.warn("Access denied: User {} attempted to delete property {}", user.getId(), id);
            throw new AccessDeniedException("You can only delete your own properties");
        }

        logger.debug("Access granted for property deletion");
    }

    /**
     * Security check before updating reviews
     * Only review author or admin can update reviews
     */
    @Before("reviewUpdatePointcut() && args(reviewId,..)")
    public void checkReviewUpdateAuthorization(Long reviewId) {
        logger.debug("Checking authorization for review update, ID: {}", reviewId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("Not authenticated");
        }

        String username = authentication.getName();
        Optional<User> currentUser = userService.getUserByEmail(username);

        if (currentUser.isEmpty()) {
            logger.warn("User not found for username: {}", username);
            throw new AccessDeniedException("User not found");
        }

        User user = currentUser.get();

        // Admin can update any review
        if (user.getRole() == User.UserRole.ADMIN) {
            logger.debug("Admin access granted for review update");
            return;
        }

        // Check if user is the review author
        Review review = getReviewById(reviewId);
        if (review == null || review.getUser() == null ||
                !review.getUser().getId().equals(user.getId())) {
            logger.warn("Access denied: User {} attempted to update review {}", user.getId(), reviewId);
            throw new AccessDeniedException("You can only update your own reviews");
        }

        logger.debug("Access granted for review update");
    }

    /**
     * Security check before deleting reviews
     * Only review author, property owner, or admin can delete reviews
     */
    @Before("reviewDeletePointcut() && args(id)")
    public void checkReviewDeleteAuthorization(Long id) {
        logger.debug("Checking authorization for review deletion, ID: {}", id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("Not authenticated");
        }

        String username = authentication.getName();
        Optional<User> currentUser = userService.getUserByEmail(username);

        if (currentUser.isEmpty()) {
            logger.warn("User not found for username: {}", username);
            throw new AccessDeniedException("User not found");
        }

        User user = currentUser.get();

        // Admin can delete any review
        if (user.getRole() == User.UserRole.ADMIN) {
            logger.debug("Admin access granted for review deletion");
            return;
        }

        Review review = getReviewById(id);
        if (review == null) {
            throw new AccessDeniedException("Review not found");
        }

        // Check if user is the review author
        if (review.getUser() != null && review.getUser().getId().equals(user.getId())) {
            logger.debug("Author access granted for review deletion");
            return;
        }

        // Check if user is the property owner
        if (review.getProperty() != null && review.getProperty().getOwner() != null &&
                review.getProperty().getOwner().getId().equals(user.getId())) {
            logger.debug("Property owner access granted for review deletion");
            return;
        }

        logger.warn("Access denied: User {} attempted to delete review {}", user.getId(), id);
        throw new AccessDeniedException("You can only delete your own reviews or reviews on your properties");
    }

    /**
     * Helper method to get property by ID
     * In a real application, this would use PropertyService directly
     */
    private Property getPropertyById(Long id) {
        // This is a placeholder. In a real application, you would use:
        // return propertyService.getPropertyById(id).orElse(null);
        return null; // Replace with actual implementation
    }

    /**
     * Helper method to get review by ID
     * In a real application, this would use ReviewService directly
     */
    private Review getReviewById(Long id) {
        // This is a placeholder. In a real application, you would use:
        // return reviewService.getReviewById(id).orElse(null);
        return null; // Replace with actual implementation
    }
}