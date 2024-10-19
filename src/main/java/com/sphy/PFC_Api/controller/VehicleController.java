package com.sphy.PFC_Api.controller;

// TODO Hacer que las búsquedas detecten si es ID o MATRÍCULA
// TODO hacer lo mismo para el delete


import com.sphy.PFC_Api.dto.VehicleDTO;
import com.sphy.PFC_Api.exception.StationNotFoundException;
import com.sphy.PFC_Api.exception.VehicleAlreadyExistException;
import com.sphy.PFC_Api.exception.VehicleNotFoundException;
import com.sphy.PFC_Api.model.ErrorResponse;
import com.sphy.PFC_Api.model.Vehicle;
import com.sphy.PFC_Api.service.RefuelService;
import com.sphy.PFC_Api.service.VehicleService;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private RefuelService refuelService;
    private Logger logger = LoggerFactory.getLogger(VehicleController.class);


    // Obtener todos los vehículos
    @GetMapping("/vehicles")
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        List<Vehicle> allVehicles = vehicleService.getAll();
        List<VehicleDTO> vehicleDTOs = allVehicles.stream()
                .map(vehicle -> {
                    VehicleDTO dto = new VehicleDTO();
                    dto.setId(vehicle.getId());
                    dto.setLicensePlate(vehicle.getLicensePlate());
                    dto.setBrand(vehicle.getBrand());
                    dto.setModel(vehicle.getModel());
                    dto.setFuel1(vehicle.getFuel1());
                    dto.setFuel2(vehicle.getFuel2());
                    dto.setKmActual(vehicle.getKmActual());
                    dto.setMedConsumption(vehicle.getMedConsumption());
                    if (vehicleService.countRefuelsByVehicleId(vehicle.getId())!=null){
                        dto.setRefuels(vehicleService.countRefuelsByVehicleId(vehicle.getId()));
                    } else dto.setRefuels(0);

                    return dto;
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(vehicleDTOs, HttpStatus.OK);
    }

    // Buscar un vehículo por ID o matrícula
    @GetMapping("/vehicles/{identifier}")
    public ResponseEntity<Vehicle> getVehicleByIdentifier(@PathVariable String identifier) throws VehicleNotFoundException {
        if (identifier.matches("\\d+")) { // Solo números
            long vehicleId = Long.parseLong(identifier);
            Optional<Vehicle> optionalVehicle = vehicleService.findById(vehicleId);
            Vehicle vehicle = optionalVehicle.orElseThrow(() -> new VehicleNotFoundException("Vehicle with ID " + vehicleId + " not found."));
            return new ResponseEntity<>(vehicle, HttpStatus.OK);
        } else { // Contiene letras
            Optional<Vehicle> optionalVehicle = vehicleService.findByLicensePlate(identifier);
            Vehicle vehicle = optionalVehicle.orElseThrow(() -> new VehicleNotFoundException("Vehicle with license plate " + identifier + " not found."));
            return new ResponseEntity<>(vehicle, HttpStatus.OK);
        }
    }


    // Añadir un nuevo vehículo
    @PostMapping("/vehicles")
    public ResponseEntity<?> saveVehicle(@Valid @RequestBody Vehicle newVehicle) {
        Optional<Vehicle> optionalVehicle = vehicleService.findByLicensePlate(newVehicle.getLicensePlate());
        if (optionalVehicle.isPresent()) {
            throw new VehicleAlreadyExistException("A vehicle with this license plate already exists.");

        }
        newVehicle.setRegistrationDate(LocalDate.now().toString());
        Vehicle savedVehicle = vehicleService.save(newVehicle);
        return new ResponseEntity<>(savedVehicle, HttpStatus.CREATED);
    }

    @PutMapping("/vehicles/{licensePlate}")
    public ResponseEntity<Vehicle> modifyVehicle(@Valid @RequestBody VehicleDTO vehicleDTO, @PathVariable String licensePlate) throws VehicleNotFoundException {
        Optional<Vehicle> optionalVehicle = vehicleService.findByLicensePlate(licensePlate);
        if (optionalVehicle.isPresent()) {
            Vehicle updatedVehicle = vehicleService.modifyVehicleByLicense(vehicleDTO, licensePlate);
            return new ResponseEntity<>(updatedVehicle, HttpStatus.OK);
        } else {
            throw new VehicleNotFoundException("Vehicle with license plate " + licensePlate + " not found.");

        }
    }








    @DeleteMapping("/vehicles/{vehicleIdentifier}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable String vehicleIdentifier) throws VehicleNotFoundException {
        if (vehicleIdentifier.matches("\\d+")) { //solo números
            long vehicleId = Long.parseLong(vehicleIdentifier);
            if (!vehicleService.existsById(vehicleId)) {
                throw new VehicleNotFoundException("Vehicle with ID " + vehicleId + " not found.");
            }
            refuelService.deleteRefuelsByVehicleId(vehicleId);
            vehicleService.deleteVehicleById(vehicleId);
        } else { // solo números
            Optional<Vehicle> optionalVehicle = vehicleService.findByLicensePlate(vehicleIdentifier);
            if (optionalVehicle.isPresent()) {
                refuelService.deleteRefuelsByVehicleId(optionalVehicle.get().getId());
                vehicleService.deleteVehicleById(optionalVehicle.get().getId());
            } else {
                throw new VehicleNotFoundException("Vehicle with license plate " + vehicleIdentifier + " not found.");
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(VehicleAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> vehicleAlreadyExistException(VehicleAlreadyExistException ex) {
        ErrorResponse errorResponse = ErrorResponse.generalError(409, ex.getMessage());
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

}
