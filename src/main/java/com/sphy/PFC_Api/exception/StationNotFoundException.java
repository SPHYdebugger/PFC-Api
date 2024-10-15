package com.sphy.PFC_Api.exception;



public class StationNotFoundException extends RuntimeException {
    public StationNotFoundException(Long id) {
        super("Station not found with ID: " + id);
    }
}
