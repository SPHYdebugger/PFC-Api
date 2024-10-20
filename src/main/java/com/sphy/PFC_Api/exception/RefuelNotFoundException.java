package com.sphy.PFC_Api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RefuelNotFoundException extends RuntimeException {

    public RefuelNotFoundException(String message) {
        super(message);
    }

    public RefuelNotFoundException(Long id) {
        super("Refuel not found with ID: " + id);
    }
}