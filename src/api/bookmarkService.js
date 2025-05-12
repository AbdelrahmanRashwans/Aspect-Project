import axios from 'axios';
import { API_BASE_URL, ENDPOINTS } from './config';

// Helper function to get auth header
const getAuthHeader = () => {
  const user = JSON.parse(localStorage.getItem('user'));
  return user && user.token ? { Authorization: `Bearer ${user.token}` } : {};
};

const bookmarkService = {
  // Get user's bookmarks
  getUserBookmarks: async (userId) => {
    try {
      const response = await axios.get(
        `${API_BASE_URL}${ENDPOINTS.USER_BOOKMARKS(userId)}`,
        { headers: getAuthHeader() }
      );
      return response.data;
    } catch (error) {
      console.error('Error fetching bookmarks:', error);
      throw error;
    }
  },

  // Add bookmark
  addBookmark: async (userId, propertyId) => {
    try {
      const response = await axios.post(
        `${API_BASE_URL}${ENDPOINTS.BOOKMARKS}`,
        { userId, propertyId },
        { headers: getAuthHeader() }
      );
      return response.data;
    } catch (error) {
      console.error('Error adding bookmark:', error);
      throw error;
    }
  },

  // Remove bookmark
  removeBookmark: async (userId, propertyId) => {
    try {
      await axios.delete(
        `${API_BASE_URL}${ENDPOINTS.BOOKMARKS}/user/${userId}/property/${propertyId}`,
        { headers: getAuthHeader() }
      );
      return true;
    } catch (error) {
      console.error('Error removing bookmark:', error);
      throw error;
    }
  },

  // Check if a property is bookmarked
  isPropertyBookmarked: async (userId, propertyId) => {
    try {
      const response = await axios.get(
        `${API_BASE_URL}${ENDPOINTS.BOOKMARKS}/check/${userId}/${propertyId}`,
        { headers: getAuthHeader() }
      );
      return response.data;
    } catch (error) {
      console.error('Error checking bookmark status:', error);
      return false;
    }
  }
};

export default bookmarkService;