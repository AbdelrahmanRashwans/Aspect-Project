import axios from 'axios';
import { API_BASE_URL, ENDPOINTS } from './config';

const propertyService = {
  // Get all properties
  getAllProperties: async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}${ENDPOINTS.PROPERTIES}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching properties:', error);
      throw error;
    }
  },

  // Get property by ID
  getPropertyById: async (id) => {
    try {
      const response = await axios.get(`${API_BASE_URL}${ENDPOINTS.PROPERTY_BY_ID(id)}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching property ${id}:`, error);
      throw error;
    }
  },

  // Search properties with filters
  searchProperties: async (filters) => {
    try {
      const response = await axios.get(`${API_BASE_URL}${ENDPOINTS.PROPERTY_SEARCH}`, {
        params: filters
      });
      return response.data;
    } catch (error) {
      console.error('Error searching properties:', error);
      throw error;
    }
  },

  // Create new property
  createProperty: async (propertyData) => {
    try {
      const response = await axios.post(`${API_BASE_URL}${ENDPOINTS.PROPERTIES}`, propertyData);
      return response.data;
    } catch (error) {
      console.error('Error creating property:', error);
      throw error;
    }
  },

  // Update property
  updateProperty: async (id, propertyData) => {
    try {
      const response = await axios.put(`${API_BASE_URL}${ENDPOINTS.PROPERTY_BY_ID(id)}`, propertyData);
      return response.data;
    } catch (error) {
      console.error(`Error updating property ${id}:`, error);
      throw error;
    }
  },

  // Delete property
  deleteProperty: async (id) => {
    try {
      await axios.delete(`${API_BASE_URL}${ENDPOINTS.PROPERTY_BY_ID(id)}`);
      return true;
    } catch (error) {
      console.error(`Error deleting property ${id}:`, error);
      throw error;
    }
  }
};

export default propertyService;