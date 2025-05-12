import React, { useState, useEffect, useContext } from 'react';
import { Container, Row, Col, Card, Badge, Button, Alert, Spinner } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import bookmarkService from '../api/bookmarkService';
import propertyService from '../api/propertyService';

const FavoritesPage = () => {
  const { currentUser, isAuthenticated } = useContext(AuthContext) || { currentUser: null, isAuthenticated: false };
  const navigate = useNavigate();

  const [bookmarkedProperties, setBookmarkedProperties] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Fetch bookmarked properties when component mounts
  useEffect(() => {
    // Redirect to login if not authenticated
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }

    fetchBookmarkedProperties();
  }, [isAuthenticated, navigate]);

  // Function to fetch all bookmarked properties
  const fetchBookmarkedProperties = async () => {
    setLoading(true);
    setError(null);

    try {
      // Get user's bookmarks
      const bookmarks = await bookmarkService.getUserBookmarks(currentUser.id);

      // Get full property details for each bookmarked property
      const propertyPromises = bookmarks.map(bookmark =>
        propertyService.getPropertyById(bookmark.propertyId)
      );

      // Resolve all promises
      const properties = await Promise.all(propertyPromises);
      setBookmarkedProperties(properties);
    } catch (err) {
      console.error('Error fetching bookmarked properties:', err);
      setError('Failed to load your favorite properties. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  // Function to remove a property from bookmarks
  const removeFromFavorites = async (propertyId) => {
    try {
      await bookmarkService.removeBookmark(currentUser.id, propertyId);
      // Update state to remove the property
      setBookmarkedProperties(bookmarkedProperties.filter(
        property => property.id !== propertyId
      ));
    } catch (err) {
      console.error('Error removing bookmark:', err);
      setError('Failed to remove property from favorites. Please try again.');
    }
  };

  // Loading state
  if (loading) {
    return (
      <Container className="py-5 text-center">
        <Spinner animation="border" variant="primary" />
        <p className="mt-2">Loading your favorite properties...</p>
      </Container>
    );
  }

  return (
    <Container className="py-4">
      <h1 className="mb-4">My Favorite Properties</h1>

      {error && <Alert variant="danger">{error}</Alert>}

      {bookmarkedProperties.length === 0 ? (
        <div className="text-center my-5">
          <p>You haven't saved any properties yet.</p>
          <Button as={Link} to="/properties" variant="primary">
            Browse Properties
          </Button>
        </div>
      ) : (
        <Row xs={1} md={2} lg={3} className="g-4">
          {bookmarkedProperties.map((property) => (
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
                <Card.Footer className="bg-white border-top-0 d-flex justify-content-between">
                  <Button
                    as={Link}
                    to={`/properties/${property.id}`}
                    variant="outline-primary"
                  >
                    View Details
                  </Button>
                  <Button
                    variant="outline-danger"
                    onClick={() => removeFromFavorites(property.id)}
                  >
                    Remove
                  </Button>
                </Card.Footer>
              </Card>
            </Col>
          ))}
        </Row>
      )}
    </Container>
  );
};

export default FavoritesPage;