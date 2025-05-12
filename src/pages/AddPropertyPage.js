import React from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import PropertyForm from '../components/property/PropertyForm';

const AddPropertyPage = () => {
  const navigate = useNavigate();

  const handleSubmit = (propertyData) => {
    // This will be connected to the propertyService later
    console.log('New property data:', propertyData);

    // For now, just simulate success and redirect
    alert('Property created successfully!');
    navigate('/properties');
  };

  return (
    <Container className="py-4">
      <Row>
        <Col>
          <h1 className="mb-4">Add New Property</h1>
          <PropertyForm onSubmit={handleSubmit} buttonText="Create Property" />
        </Col>
      </Row>
    </Container>
  );
};

export default AddPropertyPage;