package com.example.property_finder.repository;

import com.example.property_finder.model.Bookmark;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookmarkRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Bookmark> findAll() {
        return entityManager.createQuery("SELECT b FROM Bookmark b", Bookmark.class).getResultList();
    }

    public Optional<Bookmark> findById(Long id) {
        Bookmark bookmark = entityManager.find(Bookmark.class, id);
        return Optional.ofNullable(bookmark);
    }

    public List<Bookmark> findByUserId(Long userId) {
        TypedQuery<Bookmark> query = entityManager.createQuery(
                "SELECT b FROM Bookmark b WHERE b.user.id = :userId", Bookmark.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public List<Bookmark> findByPropertyId(Long propertyId) {
        TypedQuery<Bookmark> query = entityManager.createQuery(
                "SELECT b FROM Bookmark b WHERE b.property.id = :propertyId", Bookmark.class);
        query.setParameter("propertyId", propertyId);
        return query.getResultList();
    }

    public Optional<Bookmark> findByUserIdAndPropertyId(Long userId, Long propertyId) {
        try {
            TypedQuery<Bookmark> query = entityManager.createQuery(
                    "SELECT b FROM Bookmark b WHERE b.user.id = :userId AND b.property.id = :propertyId",
                    Bookmark.class);
            query.setParameter("userId", userId);
            query.setParameter("propertyId", propertyId);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public boolean existsByUserIdAndPropertyId(Long userId, Long propertyId) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(b) FROM Bookmark b WHERE b.user.id = :userId AND b.property.id = :propertyId",
                Long.class);
        query.setParameter("userId", userId);
        query.setParameter("propertyId", propertyId);
        return query.getSingleResult() > 0;
    }

    @Transactional
    public Bookmark save(Bookmark bookmark) {
        if (bookmark.getId() == null) {
            entityManager.persist(bookmark);
            return bookmark;
        } else {
            return entityManager.merge(bookmark);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        Bookmark bookmark = entityManager.find(Bookmark.class, id);
        if (bookmark != null) {
            entityManager.remove(bookmark);
        }
    }

    @Transactional
    public void deleteByUserIdAndPropertyId(Long userId, Long propertyId) {
        TypedQuery<Bookmark> query = entityManager.createQuery(
                "SELECT b FROM Bookmark b WHERE b.user.id = :userId AND b.property.id = :propertyId",
                Bookmark.class);
        query.setParameter("userId", userId);
        query.setParameter("propertyId", propertyId);

        List<Bookmark> bookmarks = query.getResultList();
        for (Bookmark bookmark : bookmarks) {
            entityManager.remove(bookmark);
        }
    }
}