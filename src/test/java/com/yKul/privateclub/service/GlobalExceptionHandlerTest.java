package com.yKul.privateclub.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFound_Returns404() {
        EntityNotFoundException ex = new EntityNotFoundException("Error");

        ResponseEntity<ErrorResponse> response = handler.handleNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Error");
    }

    @Test
    void handleBadRequest_Returns400() {
        IllegalArgumentException ex = new IllegalArgumentException("Ошибка");

        ResponseEntity<ErrorResponse> response = handler.handleBadRequest(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Ошибка");
    }

    @Test
    void handleDataIntegrityViolation_Returns400ErrorResponse() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Пользователь уже существует");

        ResponseEntity<ErrorResponse> response = handler.handleDataIntegrityViolation(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Пользователь с таким email уже существует!");
    }

    @Test
    void handleGenericException_Returns500() {
        Exception ex = new RuntimeException("Супер ошибка");

        ResponseEntity<ErrorResponse> response = handler.handleGenericException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("Ошибка сервера: Супер ошибка");
    }
}