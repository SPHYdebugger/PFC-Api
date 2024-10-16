package com.sphy.PFC_Api.controller;


import com.sphy.PFC_Api.exception.StationAlreadyExistException;
import com.sphy.PFC_Api.exception.StationNotFoundException;
import com.sphy.PFC_Api.exception.VehicleAlreadyExistException;
import com.sphy.PFC_Api.exception.VehicleNotFoundException;
import com.sphy.PFC_Api.model.ErrorResponse;
import com.sphy.PFC_Api.model.Station;
import com.sphy.PFC_Api.model.Vehicle;
import com.sphy.PFC_Api.service.RefuelService;
import com.sphy.PFC_Api.service.StationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
public class StationController {

    @Autowired
    private StationService stationService;
    @Autowired
    private RefuelService refuelService;
    private Logger logger = LoggerFactory.getLogger(StationController.class);

    // Obtener todas las estaciones
    @GetMapping("/stations")
    public ResponseEntity<List<Station>> findAllStations() {
        List<Station> allStations = stationService.getAll();
        return new ResponseEntity<>(allStations, HttpStatus.OK);
    }

    //Buscar por id o nombre
    @GetMapping("/stations/{stationIdentifier}")
    public ResponseEntity<List<Station>> findByIdentifier(@PathVariable String stationIdentifier) throws StationNotFoundException {
        if (stationIdentifier.matches("\\d+")) { //solo números
            long stationId = Long.parseLong(stationIdentifier);
            Optional<Station> optionalStation = stationService.findById(stationId);
            Station station = optionalStation.orElseThrow(() -> new StationNotFoundException("Station with ID " + stationId + " not found."));
            return new ResponseEntity<>(Collections.singletonList(station), HttpStatus.OK);
        } else { //contiene letras
            Optional<Station> optionalStation = stationService.findByName(stationIdentifier);
            Station station = optionalStation.orElseThrow(() -> new StationNotFoundException("Station with name " + stationIdentifier + " not found."));
            return new ResponseEntity<>(Collections.singletonList(station), HttpStatus.OK);
        }
    }






    // Crear una nueva estación
    @PostMapping("/stations")
    public ResponseEntity<Station> createStation(@Validated @RequestBody Station station) {
        Optional<Station> optionalStation = stationService.findByName(station.getName());
        if (optionalStation.isPresent()) {
            throw new StationAlreadyExistException("A station with this name already exists.");
        }
        station.setRegistrationDate(LocalDate.now().toString());
        stationService.save(station);
        return new ResponseEntity<>(station, HttpStatus.CREATED);
    }

    // Modificar una estación existente
    @PutMapping("/stations/{id}")
    public ResponseEntity<Station> modifyStation(@Valid @RequestBody Station station, @PathVariable long id) throws StationNotFoundException {

        Optional<Station> optionalStation = stationService.findById(id);
        if (optionalStation.isPresent()) {
            Station updatedStation = stationService.modifyStationById(station, id);
            return new ResponseEntity<>(updatedStation, HttpStatus.OK);
        } else {
            throw new StationNotFoundException("Station with id " + id + " not found.");
        }
    }

    // Eliminar una estación
    @DeleteMapping("/stations/{stationIdentifier}")
    public ResponseEntity<Void> deleteStation(@PathVariable String stationIdentifier) throws StationNotFoundException {
        if (stationIdentifier.matches("\\d+")) { //solo números
            long stationId = Long.parseLong(stationIdentifier);
            if (!stationService.existsById(stationId)) {
                throw new StationNotFoundException("Station with ID " + stationId + " not found.");
            }
            refuelService.deleteRefuelsByVehicleId(stationId);
            stationService.delete(stationId);
        } else {
            Optional<Station> optionalStation = stationService.findByName(stationIdentifier);
            if (optionalStation.isPresent()) {
                stationService.delete(optionalStation.get().getId());
            } else {
                throw new StationNotFoundException("Station with name " + stationIdentifier + " not found.");
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




    // Controlar las excepciones
    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<ErrorResponse> stationNotFoundException(StationNotFoundException pnfe) {
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

    @ExceptionHandler(StationAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> stationAlreadyExistException(StationAlreadyExistException ex) {
        ErrorResponse errorResponse = ErrorResponse.generalError(409, ex.getMessage());
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
