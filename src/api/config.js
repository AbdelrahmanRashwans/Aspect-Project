// API base URL - ensure this matches your backend
export const API_BASE_URL = 'http://localhost:8080/api';

// API endpoints - update these to match your actual backend endpoints
export const ENDPOINTS = {
  // Property endpoints
  PROPERTIES: '/properties',
  PROPERTY_BY_ID: (id) => `/properties/${id}`,
  PROPERTY_SEARCH: '/properties/search',

  // User endpoints
  USERS: '/users',
  USER_LOGIN: '/users/login',
  USER_REGISTER: '/users',

  // Bookmark endpoints
  BOOKMARKS: '/bookmarks',
  USER_BOOKMARKS: (userId) => `/bookmarks/user/${userId}`,

  // Review endpoints
  REVIEWS: '/reviews',
  PROPERTY_REVIEWS: (propertyId) => `/reviews/property/${propertyId}`,
};