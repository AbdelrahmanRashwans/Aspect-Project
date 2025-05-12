package com.example.property_finder.aspect;

import com.example.property_finder.exception.ValidationException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Aspect
@Component
public class ValidationAspect {

    private final Logger logger = LoggerFactory.getLogger(ValidationAspect.class);

    /**
     * Pointcut for all methods in the PropertyService
     */
    @Pointcut("execution(* com.example.property_finder.service.PropertyService.save*(..))")
    public void propertyServiceSavePointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Pointcut for all methods in the ReviewService
     */
    @Pointcut("execution(* com.example.property_finder.service.ReviewService.add*(..))")
    public void reviewServiceAddPointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Validates property data before saving
     */
    @Before("propertyServiceSavePointcut() && args(property,..)")
    public void validatePropertyBeforeSave(JoinPoint joinPoint, Object property) {
        logger.debug("Validating property before save in: {}", joinPoint.getSignature().getName());

        try {
            // Extract the property fields using reflection (simplified for example)
            String title = getFieldValue(property, "title", String.class);
            String description = getFieldValue(property, "description", String.class);
            BigDecimal price = getFieldValue(property, "price", BigDecimal.class);
            String location = getFieldValue(property, "location", String.class);

            // Validate title
            if (title == null || title.trim().isEmpty()) {
                throw new ValidationException("Property title cannot be empty");
            }
            if (title.length() > 100) {
                throw new ValidationException("Property title cannot exceed 100 characters");
            }

            // Validate price
            if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidationException("Property price must be greater than zero");
            }

            // Validate location
            if (location == null || location.trim().isEmpty()) {
                throw new ValidationException("Property location cannot be empty");
            }

            logger.debug("Property validation passed");

        } catch (IllegalAccessException | NoSuchFieldException e) {
            logger.error("Error during property validation", e);
            throw new ValidationException("Error validating property data");
        }
    }

    /**
     * Validates review data before adding
     */
    @Before("reviewServiceAddPointcut() && args(userId, propertyId, rating, comment, ..)")
    public void validateReviewBeforeAdd(Long userId, Long propertyId, Integer rating, String comment) {
        logger.debug("Validating review before adding");

        // Validate userId
        if (userId == null || userId <= 0) {
            throw new ValidationException("Invalid user ID");
        }

        // Validate propertyId
        if (propertyId == null || propertyId <= 0) {
            throw new ValidationException("Invalid property ID");
        }

        // Validate rating
        if (rating == null || rating < 1 || rating > 5) {
            throw new ValidationException("Rating must be between 1 and 5");
        }

        // Validate comment
        if (comment != null && comment.length() > 1000) {
            throw new ValidationException("Comment cannot exceed 1000 characters");
        }

        logger.debug("Review validation passed");
    }

    /**
     * Helper method to get field value using reflection
     */
    @SuppressWarnings("unchecked")
    private <T> T getFieldValue(Object object, String fieldName, Class<T> fieldType)
            throws NoSuchFieldException, IllegalAccessException {
        java.lang.reflect.Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(object);
    }
}