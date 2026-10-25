package com.ish.eds.globalexception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void returnsNotFoundWithHumanReadableMessage() {
        var response = handler.handleResourceNotFound(
                new ResourceNotFoundException("No eligibility case was found with case number 42."),
                request());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No eligibility case was found with case number 42.",
                response.getBody().message());
        assertEquals("/ed-api/determine/42", response.getBody().path());
    }

    @Test
    void hidesUnexpectedExceptionDetailsFromResponse() {
        var response = handler.handleUnexpectedException(
                new IllegalStateException("database credentials"), request());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Something went wrong while processing your request. Please try again later.",
                response.getBody().message());
    }

    private MockHttpServletRequest request() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/ed-api/determine/42");
        return request;
    }
}
