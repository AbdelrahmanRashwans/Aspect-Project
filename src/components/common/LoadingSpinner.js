import React from 'react';
import { Spinner, Container } from 'react-bootstrap';

const LoadingSpinner = ({ message = 'Loading...' }) => {
  return (
    <Container className="text-center my-5 py-5">
      <Spinner animation="border" variant="primary" role="status" className="mb-2">
        <span className="visually-hidden">Loading...</span>
      </Spinner>
      <p className="mt-2">{message}</p>
    </Container>
  );
};

export default LoadingSpinner;