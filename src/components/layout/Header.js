import React from 'react';
import { Link, NavLink } from 'react-router-dom';
import { Container, Navbar, Nav, Button } from 'react-bootstrap';

const Header = () => {
  // Later we can add user authentication state here
  const isLoggedIn = false;

  return (
    <Navbar bg="dark" variant="dark" expand="lg" sticky="top">
      <Container>
        <Navbar.Brand as={Link} to="/">PropertyFinder</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link as={NavLink} to="/">Home</Nav.Link>
            <Nav.Link as={NavLink} to="/properties">Properties</Nav.Link>
          </Nav>
          <Nav>
            {isLoggedIn ? (
              <>
                <Nav.Link as={NavLink} to="/favorites">Favorites</Nav.Link>
                <Nav.Link as={NavLink} to="/properties/add">Add Property</Nav.Link>
                <Nav.Link as={Button} variant="link">Logout</Nav.Link>
              </>
            ) : (
              <>
                <Nav.Link as={NavLink} to="/login">Login</Nav.Link>
                <Nav.Link as={NavLink} to="/register">Register</Nav.Link>
              </>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Header;