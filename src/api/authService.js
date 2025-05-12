import axios from 'axios';
import { API_BASE_URL, ENDPOINTS } from './config';

// Create an axios instance
const axiosInstance = axios.create({
  baseURL: API_BASE_URL
});

// Add interceptor to include token in requests
axiosInstance.interceptors.request.use(
  config => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.token) {
      config.headers.Authorization = `Bearer ${user.token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

const authService = {
  // Register a new user
  register: async (userData) => {
    try {
      const response = await axios.post(`${API_BASE_URL}${ENDPOINTS.USER_REGISTER}`, userData);
      console.log('Registration response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Registration error:', error.response?.data || error.message);
      throw error;
    }
  },

  // Login user
  login: async (credentials) => {
    try {
      const response = await axios.post(`${API_BASE_URL}${ENDPOINTS.USER_LOGIN}`, credentials);

      // Store user data in localStorage
      if (response.data) {
        localStorage.setItem('user', JSON.stringify(response.data));
      }

      return response.data;
    } catch (error) {
      console.error('Login error:', error.response?.data || error.message);
      throw error;
    }
  },

  // Logout user
  logout: () => {
    localStorage.removeItem('user');
  },

  // Get current user
  getCurrentUser: () => {
    return JSON.parse(localStorage.getItem('user'));
  },

  // Check if user is authenticated
  isAuthenticated: () => {
    const user = JSON.parse(localStorage.getItem('user'));
    return !!user && !!user.token;
  }
};

export default authService;