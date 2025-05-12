import React from 'react';
import { Container, Row, Col, Card, Button, Form } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const HomePage = () => {
  return (
    <Container>
      {/* Hero Section */}
      <div className="hero-section text-center">
        <Container>
          <h1 className="display-4 fw-bold">Find Your Dream Property</h1>
          <p className="lead">
            Browse thousands of properties for sale and rent across the country.
          </p>
          <div className="mt-4">
            <Button variant="light" size="lg" className="me-2">Browse Properties</Button>
            <Button variant="outline-light" size="lg">Learn More</Button>
          </div>
        </Container>
      </div>
      <div className="py-5 text-center">
        <h1>Find Your Dream Property</h1>
        <p className="lead">
          Browse thousands of properties for sale and rent across the country.
        </p>
      </div>

      {/* Search Form */}
      <Card className="mb-5">
        <Card.Body>
          <Form>
            <Row className="g-3">
              <Col md={4}>
                <Form.Group>
                  <Form.Label>Location</Form.Label>
                  <Form.Control type="text" placeholder="City, neighborhood, etc." />
                </Form.Group>
              </Col>
              <Col md={2}>
                <Form.Group>
                  <Form.Label>Property Type</Form.Label>
                  <Form.Select>
                    <option value="">Any</option>
                    <option value="Apartment">Apartment</option>
                    <option value="House">House</option>
                    <option value="Villa">Villa</option>
                  </Form.Select>
                </Form.Group>
              </Col>
              <Col md={2}>
                <Form.Group>
                  <Form.Label>Beds</Form.Label>
                  <Form.Select>
                    <option value="">Any</option>
                    <option value="1">1+</option>
                    <option value="2">2+</option>
                    <option value="3">3+</option>
                    <option value="4">4+</option>
                  </Form.Select>
                </Form.Group>
              </Col>
              <Col md={2}>
                <Form.Group>
                  <Form.Label>Price Range</Form.Label>
                  <Form.Select>
                    <option value="">Any</option>
                    <option value="100000-200000">$100k - $200k</option>
                    <option value="200000-300000">$200k - $300k</option>
                    <option value="300000-500000">$300k - $500k</option>
                    <option value="500000-1000000">$500k+</option>
                  </Form.Select>
                </Form.Group>
              </Col>
              <Col md={2} className="d-flex align-items-end">
                <Button variant="primary" className="w-100">Search</Button>
              </Col>
            </Row>
          </Form>
        </Card.Body>
      </Card>

      {/* Featured Properties */}
      <h2 className="mb-4">Featured Properties</h2>
      <Row xs={1} md={2} lg={3} className="g-4 mb-5">
        {[1, 2, 3].map((item) => (
          <Col key={item}>
            <Card className="h-100">
              <Card.Img variant="top" src={`https://via.placeholder.com/300x200?text=Property+${item}`} />
              <Card.Body>
                <Card.Title>Modern Apartment in Downtown</Card.Title>
                <Card.Subtitle className="mb-2 text-muted">$350,000</Card.Subtitle>
                <Card.Text>
                  Beautiful 2-bedroom apartment with amazing city views, modern amenities, and a spacious balcony.
                </Card.Text>
                <div className="d-flex justify-content-between">
                  <div><small>2 beds • 1 bath</small></div>
                  <div><small>85 m²</small></div>
                </div>
              </Card.Body>
              <Card.Footer>
                <Button as={Link} to={`/properties/${item}`} variant="outline-primary" size="sm">View Details</Button>
              </Card.Footer>
            </Card>
          </Col>
        ))}
      </Row>

      {/* How It Works */}
      <h2 className="mb-4">How It Works</h2>
      <Row className="mb-5">
        <Col md={4} className="text-center">
          <div className="mb-3">
            <span className="bg-primary text-white rounded-circle d-inline-block p-3">1</span>
          </div>
          <h4>Search Properties</h4>
          <p>Browse our extensive catalog of properties with detailed filters.</p>
        </Col>
        <Col md={4} className="text-center">
          <div className="mb-3">
            <span className="bg-primary text-white rounded-circle d-inline-block p-3">2</span>
          </div>
          <h4>Find Your Dream Home</h4>
          <p>Explore property details, images, location information, and more.</p>
        </Col>
        <Col md={4} className="text-center">
          <div className="mb-3">
            <span className="bg-primary text-white rounded-circle d-inline-block p-3">3</span>
          </div>
          <h4>Contact Agent</h4>
          <p>Connect directly with property owners or agents to arrange a viewing.</p>
        </Col>
      </Row>
    </Container>
  );
};

export default HomePage;