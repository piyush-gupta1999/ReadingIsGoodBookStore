package com.assignment.readingisgood.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class UpdateBookQuantityRequestTests {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testValidInputs() {
        UpdateBookQuantityRequest request = new UpdateBookQuantityRequest("123", 2);
        Set<ConstraintViolation<UpdateBookQuantityRequest>> violations = validator.validate(request);
        assertEquals(0, violations.size());
    }

    @Test
    public void testMissingId() {
        UpdateBookQuantityRequest request = new UpdateBookQuantityRequest("", 2);
        Set<ConstraintViolation<UpdateBookQuantityRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Id is mandatory.", violations.iterator().next().getMessage());
    }

    @Test
    public void testQuantityLessThan1() {
        UpdateBookQuantityRequest request = new UpdateBookQuantityRequest("123", 0);
        Set<ConstraintViolation<UpdateBookQuantityRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Quantity should be greater than 0.", violations.iterator().next().getMessage());
    }
}
