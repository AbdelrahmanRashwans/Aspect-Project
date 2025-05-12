import React, { useState, useEffect, useContext } from 'react';
import { Container, Row, Col, Card, Badge, Button, Tabs, Tab, ListGroup, Form, Alert, Spinner } from 'react-bootstrap';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { FaBed, FaBath, FaRulerCombined, FaMapMarkerAlt, FaRegCalendarAlt, FaHeart, FaRegHeart, FaStar } from 'react-icons/fa';
import axios from 'axios';
import { AuthContext } from '../context/AuthContext'; // Adjust path if needed
import bookmarkService from '../api/bookmarkService';

const PropertyDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { currentUser, isAuthenticated } = useContext(AuthContext) || { currentUser: null, isAuthenticated: false };

  const [property, setProperty] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [activeTab, setActiveTab] = useState('description');
  const [isFavorite, setIsFavorite] = useState(false);

  // Review states
  const [reviews, setReviews] = useState([]);
  const [reviewRating, setReviewRating] = useState(5);
  const [reviewComment, setReviewComment] = useState('');
  const [reviewSubmitting, setReviewSubmitting] = useState(false);
  const [reviewError, setReviewError] = useState(null);

  // Contact form states
  const [contactForm, setContactForm] = useState({
    name: '',
    email: '',
    phone: '',
    message: "I'm interested in this property..."
  });
  const [contactSubmitting, setContactSubmitting] = useState(false);
  const [contactSuccess, setContactSuccess] = useState(false);

  // Fetch property details
  useEffect(() => {
    const fetchPropertyData = async () => {
      setLoading(true);
      setError(null);

      try {
        // Fetch property details
        const response = await axios.get(`http://localhost:8080/api/properties/${id}`);
        setProperty(response.data);

        // Also fetch reviews
        try {
          const reviewsResponse = await axios.get(`http://localhost:8080/api/reviews/property/${id}`);
          setReviews(reviewsResponse.data);
        } catch (reviewError) {
          console.error("Error fetching reviews:", reviewError);
          // Don't set main error - just log it
        }

      } catch (err) {
        console.error("Error fetching property:", err);
        setError("Failed to load property details. Please try again.");

        // Fallback to mock data for development
        fallbackToMockData();
      } finally {
        setLoading(false);
      }
    };

    fetchPropertyData();
  }, [id]);

  // Check if property is favorited (if user is logged in)
  useEffect(() => {
    const checkFavoriteStatus = async () => {
      if (isAuthenticated && currentUser && property) {
        try {
          const response = await axios.get(
            `http://localhost:8080/api/bookmarks/check/${currentUser.id}/${property.id}`,
            { headers: getAuthHeader() }
          );
          setIsFavorite(response.data);
        } catch (err) {
          console.error("Error checking favorite status:", err);
          // Don't show error to user
        }
      }
    };

    checkFavoriteStatus();
  }, [isAuthenticated, currentUser, property]);

  useEffect(() => {
    const checkBookmarkStatus = async () => {
      if (isAuthenticated && currentUser && property) {
        try {
          const isBookmarked = await bookmarkService.isPropertyBookmarked(
            currentUser.id,
            property.id
          );
          setIsFavorite(isBookmarked);
        } catch (error) {
          console.error("Error checking bookmark status:", error);
        }
      }
    };

    checkBookmarkStatus();
  }, [isAuthenticated, currentUser, property]);
  // Helper function to get auth header
  const getAuthHeader = () => {
    const user = JSON.parse(localStorage.getItem('user'));
    return user && user.token ? { Authorization: `Bearer ${user.token}` } : {};
  };

  // Fallback to mock data if API fails
  const fallbackToMockData = () => {
    const mockProperty = {
      id: id,
      title: 'Modern Apartment in Downtown',
      price: 350000,
      location: 'Downtown, City Center',
      address: '123 Main Street, City Center, 10001',
      bedrooms: 2,
      bathrooms: 1,
      area: 85.5,
      propertyType: 'Apartment',
      listingType: 'For Sale',
      description: 'Beautiful 2-bedroom apartment with amazing city views, modern amenities, and a spacious balcony. This recently renovated unit features hardwood floors throughout, stainless steel appliances, and floor-to-ceiling windows. The building offers 24/7 security, a fitness center, and a rooftop lounge. Located in the heart of downtown, it\'s just steps away from restaurants, shopping, and public transportation.',
      features: ['Central Air Conditioning', 'Hardwood Floors', 'Stainless Steel Appliances', 'Walk-in Closet', 'Dishwasher', 'Balcony', 'High Ceilings', 'Security System'],
      images: [
        { id: 1, url: 'https://via.placeholder.com/800x600?text=Living+Room', description: 'Living Room' },
        { id: 2, url: 'https://via.placeholder.com/800x600?text=Kitchen', description: 'Kitchen' },
        { id: 3, url: 'https://via.placeholder.com/800x600?text=Bedroom', description: 'Master Bedroom' },
        { id: 4, url: 'https://via.placeholder.com/800x600?text=Bathroom', description: 'Bathroom' },
      ],
      createdAt: '2023-09-15T14:30:00',
      owner: {
        id: 1,
        name: 'John Doe',
        phone: '(123) 456-7890',
        email: 'john.doe@example.com'
      }
    };
    setProperty(mockProperty);
  };

  // Toggle favorite status
  const toggleFavorite = async () => {
    // Redirect to login if not authenticated
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }

    try {
      if (isFavorite) {
        // Remove from favorites
        await bookmarkService.removeBookmark(currentUser.id, property.id);
      } else {
        // Add to favorites
        await bookmarkService.addBookmark(currentUser.id, property.id);
      }
      // Toggle the state
      setIsFavorite(!isFavorite);
    } catch (error) {
      console.error("Error toggling bookmark:", error);
      // Optionally show error message to user
    }
  };

  // Handle review form submission
  const handleReviewSubmit = async (e) => {
    e.preventDefault();

    if (!isAuthenticated) {
      navigate('/login');
      return;
    }

    setReviewSubmitting(true);
    setReviewError(null);

    try {
      const response = await axios.post(
        `http://localhost:8080/api/reviews`,
        {
          userId: currentUser.id,
          propertyId: property.id,
          rating: reviewRating,
          comment: reviewComment
        },
        { headers: getAuthHeader() }
      );

      // Add new review to the list
      setReviews([response.data, ...reviews]);

      // Clear form
      setReviewRating(5);
      setReviewComment('');

    } catch (err) {
      console.error("Error submitting review:", err);
      setReviewError("Failed to submit review. You may have already reviewed this property.");
    } finally {
      setReviewSubmitting(false);
    }
  };

  // Handle contact form changes
  const handleContactChange = (e) => {
    const { name, value } = e.target;
    setContactForm({
      ...contactForm,
      [name]: value
    });
  };

  // Handle contact form submission
  const handleContactSubmit = async (e) => {
    e.preventDefault();
    setContactSubmitting(true);

    try {
      // In a real app, this would send the message to the backend
      await new Promise(resolve => setTimeout(resolve, 1000)); // Simulate API call
      setContactSuccess(true);
      // Reset form after successful submission
      setContactForm({
        name: '',
        email: '',
        phone: '',
        message: ''
      });
    } catch (err) {
      console.error("Error sending message:", err);
    } finally {
      setContactSubmitting(false);
    }
  };

  // Loading state
  if (loading) {
    return (
      <Container className="py-5 text-center">
        <Spinner animation="border" variant="primary" />
        <p className="mt-2">Loading property details...</p>
      </Container>
    );
  }

  // Error state
  if (error) {
    return (
      <Container className="py-5">
        <Alert variant="danger">{error}</Alert>
        <div className="text-center mt-3">
          <Button as={Link} to="/properties" variant="primary">Browse Other Properties</Button>
        </div>
      </Container>
    );
  }

  // Not found state
  if (!property) {
    return (
      <Container className="py-5 text-center">
        <h2>Property Not Found</h2>
        <p>The property you're looking for doesn't exist or has been removed.</p>
        <Button as={Link} to="/properties" variant="primary">Browse Properties</Button>
      </Container>
    );
  }

  // Get property images with fallbacks
  const propertyImages = property.images && property.images.length > 0
    ? property.images
    : [
        { id: 1, url: `https://via.placeholder.com/800x600?text=Property+${property.id}`, description: 'Property Image' }
      ];

  return (
    <Container className="py-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h1>{property.title}</h1>
          <p className="text-muted mb-0">
            <FaMapMarkerAlt className="me-1" /> {property.location}
          </p>
        </div>
        <div className="text-end">
          <h2 className="mb-0">${property.listingType === 'For Rent'
            ? `${property.price}/month`
            : property.price.toLocaleString()}</h2>
          <Badge bg={property.listingType === 'For Sale' ? 'success' : 'info'}>
            {property.listingType}
          </Badge>
        </div>
      </div>

      {/* Property Images */}
      <Row className="mb-4">
        <Col md={8}>
          <img
            src={propertyImages[0].url}
            alt={propertyImages[0].description || 'Main property image'}
            className="img-fluid rounded"
            style={{ width: '100%', height: '400px', objectFit: 'cover' }}
          />
        </Col>
        <Col md={4}>
          <Row className="g-2">
            {propertyImages.slice(1, 3).map((image, index) => (
              <Col md={12} key={image.id || index}>
                <img
                  src={image.url}
                  alt={image.description || `Property image ${index + 2}`}
                  className="img-fluid rounded"
                  style={{ width: '100%', height: '197px', objectFit: 'cover' }}
                />
              </Col>
            ))}
          </Row>
        </Col>
      </Row>

      {/* Property Details */}
      <Row>
        <Col md={8}>
          <Card className="mb-4">
            <Card.Body>
              <div className="d-flex justify-content-between mb-4">
                <div className="d-flex">
                  <div className="text-center me-4">
                    <FaBed size={24} className="text-primary" />
                    <p className="mb-0 mt-1">{property.bedrooms} Beds</p>
                  </div>
                  <div className="text-center me-4">
                    <FaBath size={24} className="text-primary" />
                    <p className="mb-0 mt-1">{property.bathrooms} Baths</p>
                  </div>
                  <div className="text-center">
                    <FaRulerCombined size={24} className="text-primary" />
                    <p className="mb-0 mt-1">{property.area} m²</p>
                  </div>
                </div>
                <div>
                  <Button
                    variant="outline-danger"
                    onClick={toggleFavorite}
                    className="d-flex align-items-center"
                  >
                    {isFavorite ? <FaHeart className="me-2" /> : <FaRegHeart className="me-2" />}
                    {isFavorite ? 'Saved' : 'Save'}
                  </Button>
                </div>
              </div>

              <Tabs
                activeKey={activeTab}
                onSelect={(k) => setActiveTab(k)}
                className="mb-3"
              >
                <Tab eventKey="description" title="Description">
                  <p>{property.description}</p>
                </Tab>
                <Tab eventKey="features" title="Features">
                  <div className="row">
                    {property.features && property.features.map ? (
                      property.features.map((feature, index) => (
                        <div className="col-md-6 mb-2" key={index}>
                          <span className="text-primary me-2">✓</span> {feature}
                        </div>
                      ))
                    ) : (
                      <p>No features listed for this property.</p>
                    )}
                  </div>
                </Tab>
                <Tab eventKey="location" title="Location">
                  <p className="mb-3">{property.address || property.location}</p>
                  <div className="bg-light p-3 text-center rounded">
                    <p className="mb-0">Map will be displayed here</p>
                  </div>
                </Tab>
              </Tabs>
            </Card.Body>
          </Card>

          {/* Reviews Section */}
          <Card className="mb-4">
            <Card.Header as="h5">Reviews</Card.Header>
            <Card.Body>
              {reviews && reviews.length > 0 ? (
                <div className="mb-4">
                  {reviews.map(review => (
                    <div key={review.id} className="border-bottom mb-3 pb-3">
                      <div className="d-flex justify-content-between">
                        <div>
                          <div className="d-flex align-items-center mb-1">
                            {[...Array(5)].map((_, i) => (
                              <FaStar key={i} className={i < review.rating ? "text-warning" : "text-muted"} />
                            ))}
                            <span className="ms-2 fw-bold">{review.userName || 'Anonymous User'}</span>
                          </div>
                          <p className="mb-0">{review.comment}</p>
                        </div>
                        <small className="text-muted">
                          {new Date(review.createdAt).toLocaleDateString()}
                        </small>
                      </div>
                    </div>
                  ))}
                </div>
              ) : (
                <div className="mb-4">
                  <p>No reviews yet. Be the first to review this property!</p>
                </div>
              )}

              <h6>Add Review</h6>
              {reviewError && <Alert variant="danger">{reviewError}</Alert>}

              <Form onSubmit={handleReviewSubmit}>
                <Form.Group className="mb-3">
                  <Form.Label>Rating</Form.Label>
                  <Form.Select
                    value={reviewRating}
                    onChange={(e) => setReviewRating(Number(e.target.value))}
                    required
                  >
                    <option value="5">5 - Excellent</option>
                    <option value="4">4 - Very Good</option>
                    <option value="3">3 - Good</option>
                    <option value="2">2 - Fair</option>
                    <option value="1">1 - Poor</option>
                  </Form.Select>
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Comment</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={3}
                    placeholder="Share your experience with this property..."
                    value={reviewComment}
                    onChange={(e) => setReviewComment(e.target.value)}
                    required
                  />
                </Form.Group>
                <Button
                  variant="primary"
                  type="submit"
                  disabled={reviewSubmitting || !isAuthenticated}
                >
                  {reviewSubmitting ? 'Submitting...' : 'Submit Review'}
                </Button>
                {!isAuthenticated && (
                  <small className="d-block mt-2 text-muted">
                    Please <Link to="/login">log in</Link> to leave a review.
                  </small>
                )}
              </Form>
            </Card.Body>
          </Card>
        </Col>

        <Col md={4}>
          {/* Contact Card */}
          <Card className="mb-4">
            <Card.Header as="h5">Contact Agent</Card.Header>
            <Card.Body>
              <div className="mb-3">
                <h6>{property.owner?.name || 'Property Agent'}</h6>
                <p className="mb-2">{property.owner?.phone || 'Contact information unavailable'}</p>
                {property.owner?.email && <p className="mb-0">{property.owner.email}</p>}
              </div>

              {contactSuccess ? (
                <Alert variant="success">
                  Your message has been sent successfully! The agent will contact you soon.
                </Alert>
              ) : (
                <Form onSubmit={handleContactSubmit}>
                  <Form.Group className="mb-3">
                    <Form.Label>Your Name</Form.Label>
                    <Form.Control
                      type="text"
                      name="name"
                      value={contactForm.name}
                      onChange={handleContactChange}
                      placeholder="Enter your name"
                      required
                    />
                  </Form.Group>
                  <Form.Group className="mb-3">
                    <Form.Label>Your Email</Form.Label>
                    <Form.Control
                      type="email"
                      name="email"
                      value={contactForm.email}
                      onChange={handleContactChange}
                      placeholder="Enter your email"
                      required
                    />
                  </Form.Group>
                  <Form.Group className="mb-3">
                    <Form.Label>Your Phone</Form.Label>
                    <Form.Control
                      type="tel"
                      name="phone"
                      value={contactForm.phone}
                      onChange={handleContactChange}
                      placeholder="Enter your phone number"
                    />
                  </Form.Group>
                  <Form.Group className="mb-3">
                    <Form.Label>Message</Form.Label>
                    <Form.Control
                      as="textarea"
                      rows={3}
                      name="message"
                      value={contactForm.message}
                      onChange={handleContactChange}
                      placeholder="I'm interested in this property..."
                      required
                    />
                  </Form.Group>
                  <Button
                    variant="primary"
                    type="submit"
                    className="w-100"
                    disabled={contactSubmitting}
                  >
                    {contactSubmitting ? 'Sending...' : 'Send Message'}
                  </Button>
                </Form>
              )}
            </Card.Body>
          </Card>

          {/* Property Details Card */}
          <Card>
            <Card.Header as="h5">Property Details</Card.Header>
            <ListGroup variant="flush">
              <ListGroup.Item>
                <div className="d-flex justify-content-between">
                  <span>Property Type:</span>
                  <span className="fw-bold">{property.propertyType}</span>
                </div>
              </ListGroup.Item>
              <ListGroup.Item>
                <div className="d-flex justify-content-between">
                  <span>Listing Type:</span>
                  <span className="fw-bold">{property.listingType}</span>
                </div>
              </ListGroup.Item>
              <ListGroup.Item>
                <div className="d-flex justify-content-between">
                  <span>Bedrooms:</span>
                  <span className="fw-bold">{property.bedrooms}</span>
                </div>
              </ListGroup.Item>
              <ListGroup.Item>
                <div className="d-flex justify-content-between">
                  <span>Bathrooms:</span>
                  <span className="fw-bold">{property.bathrooms}</span>
                </div>
              </ListGroup.Item>
              <ListGroup.Item>
                <div className="d-flex justify-content-between">
                  <span>Area:</span>
                  <span className="fw-bold">{property.area} m²</span>
                </div>
              </ListGroup.Item>
              <ListGroup.Item>
                <div className="d-flex justify-content-between">
                  <span>Listed on:</span>
                  <span className="fw-bold">
                    <FaRegCalendarAlt className="me-1" />
                    {new Date(property.createdAt).toLocaleDateString()}
                  </span>
                </div>
              </ListGroup.Item>
            </ListGroup>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default PropertyDetailPage;