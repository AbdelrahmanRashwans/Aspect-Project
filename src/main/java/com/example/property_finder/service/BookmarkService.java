package com.example.property_finder.service;

import com.example.property_finder.model.Bookmark;
import com.example.property_finder.model.Property;
import com.example.property_finder.model.User;
import com.example.property_finder.repository.BookmarkRepository;
import com.example.property_finder.repository.PropertyRepository;
import com.example.property_finder.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository,
                           UserRepository userRepository,
                           PropertyRepository propertyRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
    }

    public List<Bookmark> getAllBookmarks() {
        return bookmarkRepository.findAll();
    }

    public Optional<Bookmark> getBookmarkById(Long id) {
        return bookmarkRepository.findById(id);
    }

    public List<Bookmark> getBookmarksByUserId(Long userId) {
        return bookmarkRepository.findByUserId(userId);
    }

    public List<Bookmark> getBookmarksByPropertyId(Long propertyId) {
        return bookmarkRepository.findByPropertyId(propertyId);
    }

    public boolean isPropertyBookmarkedByUser(Long userId, Long propertyId) {
        return bookmarkRepository.existsByUserIdAndPropertyId(userId, propertyId);
    }

    @Transactional
    public Optional<Bookmark> addBookmark(Long userId, Long propertyId) {
        // Check if bookmark already exists
        if (bookmarkRepository.existsByUserIdAndPropertyId(userId, propertyId)) {
            return Optional.empty(); // Bookmark already exists
        }

        // Get user and property
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Property> propertyOptional = propertyRepository.findById(propertyId);

        if (userOptional.isPresent() && propertyOptional.isPresent()) {
            User user = userOptional.get();
            Property property = propertyOptional.get();

            Bookmark bookmark = new Bookmark(user, property);
            Bookmark savedBookmark = bookmarkRepository.save(bookmark);
            return Optional.of(savedBookmark);
        }

        return Optional.empty();
    }

    @Transactional
    public boolean removeBookmark(Long userId, Long propertyId) {
        if (bookmarkRepository.existsByUserIdAndPropertyId(userId, propertyId)) {
            bookmarkRepository.deleteByUserIdAndPropertyId(userId, propertyId);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean removeBookmarkById(Long id) {
        if (bookmarkRepository.findById(id).isPresent()) {
            bookmarkRepository.deleteById(id);
            return true;
        }
        return false;
    }
}