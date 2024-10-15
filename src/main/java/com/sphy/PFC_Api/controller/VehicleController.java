package com.sphy.PFC_Api.controller;




import com.sphy.PFC_Api.exception.VehicleAlreadyExistException;
import com.sphy.PFC_Api.exception.VehicleNotFoundException;
import com.sphy.PFC_Api.model.ErrorResponse;
import com.sphy.PFC_Api.model.Vehicle;
import com.sphy.PFC_Api.service.VehicleService;

import jakarta.validation.Valid;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    private Logger logger = LoggerFactory.getLogger(VehicleController.class);


    // Obtener todos los Vehículos o filtrar uno por matrícula
    @GetMapping("/vehicles")
    public ResponseEntity<List<Vehicle>> findAll(
            @RequestParam(defaultValue = "") String licensePlate
    ) throws VehicleNotFoundException {
        if (!licensePlate.isEmpty()) {
            Optional<Vehicle> optionalVehicle = vehicleService.findByLicensePlate(licensePlate);
            Vehicle vehicle = optionalVehicle.orElseThrow(() -> new VehicleNotFoundException(licensePlate));
            return new ResponseEntity<>(Collections.singletonList(vehicle), HttpStatus.OK);
        } else {
            List<Vehicle> allVehicles = vehicleService.getAll();
            return new ResponseEntity<>(allVehicles, HttpStatus.OK);
        }



    }

    // Añadir un nuevo vehículo
    @PostMapping("/vehicles")
    public ResponseEntity<?> saveVehicle(@Valid @RequestBody Vehicle newVehicle) {
        Optional<Vehicle> optionalVehicle = vehicleService.findByLicensePlate(newVehicle.getLicensePlate());
        if (optionalVehicle.isPresent()) {
            throw new VehicleAlreadyExistException("A vehicle with this license plate already exists.");
        }
        vehicleService.save(newVehicle);
        return new ResponseEntity<>(newVehicle, HttpStatus.CREATED);
    }


    // Borrar un vehículo por la matrícula
    @DeleteMapping("/vehicles/{licensePlate}")
    public ResponseEntity<Void> removeVehicle(@PathVariable String licensePlate) throws VehicleNotFoundException {
        Optional<Vehicle> optionalVehicle = vehicleService.findByLicensePlate(licensePlate);
        if (optionalVehicle.isPresent()) {
            vehicleService.deleteVehicleByLicensePlate(licensePlate);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new VehicleNotFoundException(" Already exist a vehicle with licensePlate: " + licensePlate);
        }
    }



    // Modificar un vehículo por matrícula
    @PutMapping("/vehicles/{licensePlate}")
    public ResponseEntity<Vehicle> modifyVehicle(@Valid @RequestBody Vehicle vehicle, @PathVariable String licensePlate) throws VehicleNotFoundException {

        Optional<Vehicle> optionalVehicle = vehicleService.findByLicensePlate(licensePlate);
        if (optionalVehicle.isPresent()) {
            vehicleService.modifyVehicleByLicense(vehicle, licensePlate);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new VehicleNotFoundException("Vehicle with license plate " + licensePlate + " not found.");
        }
    }




    // Control de excepciones
    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ErrorResponse> vehicleNotFoundException(VehicleNotFoundException pnfe) {
        ErrorResponse errorResponse = ErrorResponse.generalError(404, pnfe.getMessage());
        logger.error(pnfe.getMessage(), pnfe);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manve) {
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }







}
