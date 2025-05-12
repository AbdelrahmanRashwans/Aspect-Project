import React from 'react';
import { Container, Row, Col } from 'react-bootstrap';

const Footer = () => {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="bg-dark text-light py-4 mt-5">
      <Container>
        <Row>
          <Col md={4}>
            <h5>PropertyFinder</h5>
            <p>Find your dream property with ease.</p>
          </Col>
          <Col md={4}>
            <h5>Quick Links</h5>
            <ul className="list-unstyled">
              <li><a href="/" className="text-light">Home</a></li>
              <li><a href="/properties" className="text-light">Properties</a></li>
              <li><a href="/about" className="text-light">About Us</a></li>
              <li><a href="/contact" className="text-light">Contact</a></li>
            </ul>
          </Col>
          <Col md={4}>
            <h5>Contact Us</h5>
            <address>
              123 Property Street<br />
              Real Estate City<br />
              contact@propertyfinder.com
            </address>
          </Col>
        </Row>
        <hr className="bg-light" />
        <div className="text-center">
          <p>&copy; {currentYear} PropertyFinder. All rights reserved.</p>
        </div>
      </Container>
    </footer>
  );
};

export default Footer;