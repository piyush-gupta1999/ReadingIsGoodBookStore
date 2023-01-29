package com.assignment.readingisgood.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class AddBookRequestTests {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testValidInputs() {
        AddBookRequest request = new AddBookRequest("Harry Potter", "J.K. Rowling", 2, 20.99);
        Set<ConstraintViolation<AddBookRequest>> violations = validator.validate(request);
        assertEquals(0, violations.size());
    }

    @Test
    public void testMissingBookName() {
        AddBookRequest request = new AddBookRequest("", "J.K. Rowling", 2, 20.99);
        Set<ConstraintViolation<AddBookRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Book name is mandatory.", violations.iterator().next().getMessage());
    }

    @Test
    public void testMissingAuthorName() {
        AddBookRequest request = new AddBookRequest("Harry Potter", "", 2, 20.99);
        Set<ConstraintViolation<AddBookRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Author name is mandatory.", violations.iterator().next().getMessage());
    }

    @Test
    public void testQuantityLessThan1() {
        AddBookRequest request = new AddBookRequest("Harry Potter", "J.K. Rowling", 0, 20.99);
        Set<ConstraintViolation<AddBookRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Quantity should be greater than 0.", violations.iterator().next().getMessage());
    }

    @Test
    public void testNegativeQuantity() {
        AddBookRequest request = new AddBookRequest("Harry Potter", "J.K. Rowling", -10, 20.99);
        Set<ConstraintViolation<AddBookRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Quantity should be greater than 0.", violations.iterator().next().getMessage());
    }

    @Test
    public void testPriceLessThan1() {
        AddBookRequest request = new AddBookRequest("Harry Potter", "J.K. Rowling", 2, 0.99);
        Set<ConstraintViolation<AddBookRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Price should be grater than 1.00.", violations.iterator().next().getMessage());
    }

    @Test
    public void testNegativePrice() {
        AddBookRequest request = new AddBookRequest("Harry Potter", "J.K. Rowling", 2, -0.99);
        Set<ConstraintViolation<AddBookRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Price should be grater than 1.00.", violations.iterator().next().getMessage());
    }
}

