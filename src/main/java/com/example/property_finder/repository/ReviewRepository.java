package com.example.property_finder.repository;

import com.example.property_finder.model.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReviewRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Review> findAll() {
        return entityManager.createQuery("SELECT r FROM Review r", Review.class).getResultList();
    }

    public Optional<Review> findById(Long id) {
        Review review = entityManager.find(Review.class, id);
        return Optional.ofNullable(review);
    }

    public List<Review> findByPropertyId(Long propertyId) {
        TypedQuery<Review> query = entityManager.createQuery(
                "SELECT r FROM Review r WHERE r.property.id = :propertyId ORDER BY r.createdAt DESC",
                Review.class);
        query.setParameter("propertyId", propertyId);
        return query.getResultList();
    }

    public List<Review> findByUserId(Long userId) {
        TypedQuery<Review> query = entityManager.createQuery(
                "SELECT r FROM Review r WHERE r.user.id = :userId ORDER BY r.createdAt DESC",
                Review.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public Optional<Review> findByUserIdAndPropertyId(Long userId, Long propertyId) {
        try {
            TypedQuery<Review> query = entityManager.createQuery(
                    "SELECT r FROM Review r WHERE r.user.id = :userId AND r.property.id = :propertyId",
                    Review.class);
            query.setParameter("userId", userId);
            query.setParameter("propertyId", propertyId);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Double getAverageRatingByPropertyId(Long propertyId) {
        TypedQuery<Double> query = entityManager.createQuery(
                "SELECT AVG(r.rating) FROM Review r WHERE r.property.id = :propertyId",
                Double.class);
        query.setParameter("propertyId", propertyId);
        return query.getSingleResult();
    }

    public Long getReviewCountByPropertyId(Long propertyId) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(r) FROM Review r WHERE r.property.id = :propertyId",
                Long.class);
        query.setParameter("propertyId", propertyId);
        return query.getSingleResult();
    }

    public List<Review> findRecentReviews(int limit) {
        TypedQuery<Review> query = entityManager.createQuery(
                "SELECT r FROM Review r ORDER BY r.createdAt DESC",
                Review.class);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Transactional
    public Review save(Review review) {
        if (review.getId() == null) {
            entityManager.persist(review);
            return review;
        } else {
            return entityManager.merge(review);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        Review review = entityManager.find(Review.class, id);
        if (review != null) {
            entityManager.remove(review);
        }
    }
}