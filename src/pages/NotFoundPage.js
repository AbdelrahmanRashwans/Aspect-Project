import React from 'react';
import { Container, Row, Col, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const NotFoundPage = () => {
  return (
    <Container>
      <Row className="justify-content-center text-center py-5">
        <Col md={6}>
          <h1 className="display-1">404</h1>
          <h2>Page Not Found</h2>
          <p className="mb-4">The page you are looking for doesn't exist or has been moved.</p>
          <Button as={Link} to="/" variant="primary">Go Home</Button>
        </Col>
      </Row>
    </Container>
  );
};

export default NotFoundPage;