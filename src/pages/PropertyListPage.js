import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Form, Badge, Alert } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import axios from 'axios';

const PropertyListPage = () => {
  // State for properties and loading
  const [properties, setProperties] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // State for filters
  const [filters, setFilters] = useState({
    location: '',
    propertyType: '',
    listingType: '',
    bedrooms: '',
    minPrice: '',
    maxPrice: ''
  });

  // Fetch properties on component mount
  useEffect(() => {
    fetchProperties();
  }, []);

  // Function to fetch properties from API
  const fetchProperties = async () => {
    setLoading(true);
    setError(null);

    try {
      const response = await axios.get('http://localhost:8080/api/properties');
      setProperties(response.data);
    } catch (err) {
      console.error("Error fetching properties:", err);
      setError("Failed to load properties. Please try again.");

      // Fallback to mock data if API fails
      fallbackToMockData();
    } finally {
      setLoading(false);
    }
  };

  // Fallback to mock data if API fails
  const fallbackToMockData = () => {
    const mockProperties = [
      {
        id: 1,
        title: 'Modern Apartment in Downtown',
        price: 350000,
        location: 'Downtown, City Center',
        bedrooms: 2,
        bathrooms: 1,
        area: 85.5,
        propertyType: 'Apartment',
        listingType: 'For Sale',
        description: 'Beautiful 2-bedroom apartment with amazing city views, modern amenities, and a spacious balcony.',
        imageUrl: 'https://via.placeholder.com/300x200?text=Property+1'
      },
      // ... rest of your mock properties
    ];
    setProperties(mockProperties);
  };

  // Handle filter input changes
  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters({
      ...filters,
      [name]: value
    });
  };

  // Apply filters
  const applyFilters = async () => {
    setLoading(true);
    setError(null);

    try {
      // Remove empty filter values
      const activeFilters = Object.fromEntries(
        Object.entries(filters).filter(([_, value]) => value !== '')
      );

      // Build query string
      const queryParams = new URLSearchParams();
      if (activeFilters.location) queryParams.append('location', activeFilters.location);
      if (activeFilters.propertyType) queryParams.append('propertyType', activeFilters.propertyType);
      if (activeFilters.listingType) queryParams.append('listingType', activeFilters.listingType);
      if (activeFilters.bedrooms) queryParams.append('bedrooms', activeFilters.bedrooms);
      if (activeFilters.minPrice) queryParams.append('minPrice', activeFilters.minPrice);
      if (activeFilters.maxPrice) queryParams.append('maxPrice', activeFilters.maxPrice);

      // Make API call with filters
      const response = await axios.get(`http://localhost:8080/api/properties/search?${queryParams}`);
      setProperties(response.data);
    } catch (err) {
      console.error("Error searching properties:", err);
      setError("Failed to search properties. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container>
      <h1 className="mb-4">Properties</h1>

      {error && (
        <Alert variant="danger" className="mb-4">
          {error}
          <Button
            variant="outline-danger"
            size="sm"
            className="ms-3"
            onClick={fetchProperties}
          >
            Try Again
          </Button>
        </Alert>
      )}

      {/* Filters */}
      <Card className="mb-4">
        <Card.Body>
          <Form>
            <Row className="g-3">
              <Col md={3}>
                <Form.Group>
                  <Form.Label>Location</Form.Label>
                  <Form.Control
                    type="text"
                    name="location"
                    value={filters.location}
                    onChange={handleFilterChange}
                    placeholder="City, neighborhood, etc."
                  />
                </Form.Group>
              </Col>
              <Col md={2}>
                <Form.Group>
                  <Form.Label>Property Type</Form.Label>
                  <Form.Select
                    name="propertyType"
                    value={filters.propertyType}
                    onChange={handleFilterChange}
                  >
                    <option value="">Any</option>
                    <option value="Apartment">Apartment</option>
                    <option value="House">House</option>
                    <option value="Villa">Villa</option>
                    <option value="Penthouse">Penthouse</option>
                    <option value="Commercial">Commercial</option>
                  </Form.Select>
                </Form.Group>
              </Col>
              <Col md={2}>
                <Form.Group>
                  <Form.Label>Listing Type</Form.Label>
                  <Form.Select
                    name="listingType"
                    value={filters.listingType}
                    onChange={handleFilterChange}
                  >
                    <option value="">Any</option>
                    <option value="For Sale">For Sale</option>
                    <option value="For Rent">For Rent</option>
                  </Form.Select>
                </Form.Group>
              </Col>
              <Col md={2}>
                <Form.Group>
                  <Form.Label>Bedrooms</Form.Label>
                  <Form.Select
                    name="bedrooms"
                    value={filters.bedrooms}
                    onChange={handleFilterChange}
                  >
                    <option value="">Any</option>
                    <option value="0">Studio</option>
                    <option value="1">1+</option>
                    <option value="2">2+</option>
                    <option value="3">3+</option>
                    <option value="4">4+</option>
                  </Form.Select>
                </Form.Group>
              </Col>
              <Col md={3}>
                <Form.Group>
                  <Form.Label>Price Range</Form.Label>
                  <Row>
                    <Col>
                      <Form.Control
                        type="number"
                        name="minPrice"
                        value={filters.minPrice}
                        onChange={handleFilterChange}
                        placeholder="Min"
                      />
                    </Col>
                    <Col>
                      <Form.Control
                        type="number"
                        name="maxPrice"
                        value={filters.maxPrice}
                        onChange={handleFilterChange}
                        placeholder="Max"
                      />
                    </Col>
                  </Row>
                </Form.Group>
              </Col>
            </Row>
            <Row className="mt-3">
              <Col className="d-flex justify-content-end">
                <Button variant="primary" onClick={applyFilters}>
                  Apply Filters
                </Button>
              </Col>
            </Row>
          </Form>
        </Card.Body>
      </Card>

      {/* Property List */}
      {loading ? (
        <div className="text-center my-5">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
          <p className="mt-2">Loading properties...</p>
        </div>
      ) : (
        <Row xs={1} md={2} lg={3} className="g-4">
          {properties.map((property) => (
            <Col key={property.id}>
              <Card className="h-100 property-card">
                <Card.Img
                  variant="top"
                  src={property.imageUrl || `https://via.placeholder.com/300x200?text=Property+${property.id}`}
                />
                <Card.Body>
                  <Card.Title>{property.title}</Card.Title>
                  <div className="d-flex justify-content-between align-items-center mb-2">
                    <span className="property-price">
                      ${property.listingType === 'For Rent'
                        ? `${property.price}/month`
                        : property.price.toLocaleString()}
                    </span>
                    <Badge bg={property.listingType === 'For Sale' ? 'success' : 'info'}>
                      {property.listingType}
                    </Badge>
                  </div>
                  <Card.Text className="mb-3">
                    {property.description
                      ? property.description.substring(0, 100) + '...'
                      : 'No description available'}
                  </Card.Text>
                  <div className="d-flex justify-content-between text-muted small mb-3">
                    <div><i className="fa fa-bed me-1"></i> {property.bedrooms} beds</div>
                    <div><i className="fa fa-bath me-1"></i> {property.bathrooms} bath</div>
                    <div><i className="fa fa-ruler-combined me-1"></i> {property.area} m²</div>
                  </div>
                </Card.Body>
                <Card.Footer className="bg-white border-top-0">
                  <Button
                    as={Link}
                    to={`/properties/${property.id}`}
                    variant="outline-primary"
                    className="w-100"
                  >
                    View Details
                  </Button>
                </Card.Footer>
              </Card>
            </Col>
          ))}
        </Row>
      )}

      {properties.length === 0 && !loading && (
        <div className="text-center my-5">
          <p>No properties found matching your criteria.</p>
          <Button variant="primary" onClick={fetchProperties}>
            View All Properties
          </Button>
        </div>
      )}
    </Container>
  );
};

export default PropertyListPage;