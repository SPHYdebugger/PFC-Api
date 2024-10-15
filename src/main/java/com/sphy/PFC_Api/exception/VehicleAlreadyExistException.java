package com.sphy.PFC_Api.exception;

public class VehicleAlreadyExistException extends RuntimeException {

    public VehicleAlreadyExistException(String message) {
        super(message);
    }

}