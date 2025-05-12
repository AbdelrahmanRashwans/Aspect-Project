import React, { createContext, useState, useEffect } from 'react';
import authService from '../api/authService';

// Create context
export const AuthContext = createContext();

// Provider component
export const AuthProvider = ({ children }) => {
  const [currentUser, setCurrentUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  // Check if user is logged in on initial load
  useEffect(() => {
    const initAuth = () => {
      const user = authService.getCurrentUser();
      if (user) {
        setCurrentUser(user);
        setIsAuthenticated(true);
      }
      setLoading(false);
    };

    initAuth();
  }, []);

  // Login function
  const login = (email, password) => {
    return authService.login({ email, password })
      .then(user => {
        setCurrentUser(user);
        setIsAuthenticated(true);
        return user;
      });
  };

  // Register function
  const register = (userData) => {
    return authService.register(userData)
      .then(user => {
        setCurrentUser(user);
        setIsAuthenticated(true);
        return user;
      });
  };

  // Logout function
  const logout = () => {
    authService.logout();
    setCurrentUser(null);
    setIsAuthenticated(false);
  };

  // Context values to provide
  const value = {
    currentUser,
    isAuthenticated,
    loading,
    login,
    register,
    logout
  };

  return (
    <AuthContext.Provider value={value}>
      {!loading && children}
    </AuthContext.Provider>
  );
};