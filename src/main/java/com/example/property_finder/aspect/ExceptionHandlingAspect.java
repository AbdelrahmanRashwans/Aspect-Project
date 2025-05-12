package com.example.property_finder.aspect;

import com.example.property_finder.exception.AccessDeniedException;
import com.example.property_finder.exception.ResourceNotFoundException;
import com.example.property_finder.exception.ValidationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionHandlingAspect {

    private final Logger logger = LoggerFactory.getLogger(ExceptionHandlingAspect.class);

    /**
     * Pointcut for all service methods
     */
    @Pointcut("execution(* com.example.property_finder.service.*.*(..))")
    public void serviceLayer() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Centralized exception handling for service layer
     */
    @Around("serviceLayer()")
    public Object handleExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (ValidationException e) {
            // Log and rethrow validation exceptions
            logger.warn("Validation error in {}.{}: {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    e.getMessage());
            throw e;
        } catch (AccessDeniedException e) {
            // Log and rethrow access denied exceptions
            logger.warn("Access denied in {}.{}: {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    e.getMessage());
            throw e;
        } catch (ResourceNotFoundException e) {
            // Log and rethrow not found exceptions
            logger.info("Resource not found in {}.{}: {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    e.getMessage());
            throw e;
        } catch (DataAccessException e) {
            // Log database exceptions
            logger.error("Database error in {}.{}: {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    e.getMessage());
            // Transform to a custom application exception
            throw new RuntimeException("A database error occurred", e);
        } catch (Exception e) {
            // Log unexpected exceptions
            logger.error("Unexpected error in {}.{}: {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    e.getMessage(), e);
            // Transform to a general application exception
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }
}