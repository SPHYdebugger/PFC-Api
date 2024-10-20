package com.sphy.PFC_Api.controller;

import com.sphy.PFC_Api.dto.RefuelDTO;
import com.sphy.PFC_Api.exception.RefuelNotFoundException;
import com.sphy.PFC_Api.exception.StationNotFoundException;
import com.sphy.PFC_Api.exception.VehicleNotFoundException;
import com.sphy.PFC_Api.model.ErrorResponse;
import com.sphy.PFC_Api.model.Refuel;
import com.sphy.PFC_Api.model.Station;
import com.sphy.PFC_Api.model.Vehicle;
import com.sphy.PFC_Api.service.RefuelService;
import com.sphy.PFC_Api.service.StationService;
import com.sphy.PFC_Api.service.VehicleService;
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
@RequestMapping
@Validated
public class RefuelController {

    private final RefuelService refuelService;
    private VehicleService vehicleService;
    private StationService stationService;
    private Logger logger = LoggerFactory.getLogger(RefuelController.class);

    @Autowired
    public RefuelController(RefuelService refuelService, VehicleService vehicleService, StationService stationService) {
        this.refuelService = refuelService;
        this.vehicleService = vehicleService;
        this.stationService = stationService;
    }

    @GetMapping("/refuels")
    public ResponseEntity<List<RefuelDTO>> findAll(){
        List<RefuelDTO> allrefuels = refuelService.getAllRefuels();
        return new ResponseEntity<>(allrefuels, HttpStatus.OK);
    }

    // Buscar Refuel por id, nombre de la estación o matrícula del vehículo
    @GetMapping("/refuels/{refuelIdentifier}")
    public ResponseEntity<List<Refuel>> findRefuelByIdentifier(@PathVariable String refuelIdentifier) throws RefuelNotFoundException {
        // Si el identificador es solo números, buscar por ID
        if (refuelIdentifier.matches("\\d+")) {
            long refuelId = Long.parseLong(refuelIdentifier);
            Optional<Refuel> optionalRefuel = refuelService.findById(refuelId);
            Refuel refuel = optionalRefuel.orElseThrow(() -> new RefuelNotFoundException("Refuel with ID " + refuelId + " not found."));
            return new ResponseEntity<>(Collections.singletonList(refuel), HttpStatus.OK);

            // Si el identificador es solo letras, buscar por nombre de la estación
        } else if (refuelIdentifier.matches("[a-zA-Z]+")) {
            List<Refuel> refuelsByStationName = refuelService.getRefuelsByStationName(refuelIdentifier);
            if (refuelsByStationName.isEmpty()) {
                throw new RefuelNotFoundException("No refuels found for station with name " + refuelIdentifier);
            }
            return new ResponseEntity<>(refuelsByStationName, HttpStatus.OK);

            // Si el identificador es una combinación de letras y números, buscar por matrícula del vehículo
        } else if (refuelIdentifier.matches("[a-zA-Z0-9]+")) {
            List<Refuel> refuelsByLicensePlate = refuelService.getRefuelsByLicensePlate(refuelIdentifier);
            if (refuelsByLicensePlate.isEmpty()) {
                throw new RefuelNotFoundException("No refuels found for vehicle with license plate " + refuelIdentifier);
            }
            return new ResponseEntity<>(refuelsByLicensePlate, HttpStatus.OK);
        }
        throw new RefuelNotFoundException("Invalid refuel identifier format.");
    }

    @PostMapping("refuels/{vehicleId}/{stationId}")
    public ResponseEntity<Refuel> addRefuel(
            @PathVariable long vehicleId,
            @PathVariable long stationId,
            @Valid @RequestBody Refuel refuel) {

        int traveledKms = refuel.getKmTotal() - refuelService.getTotalKmsByVehicleId(vehicleId);
        float fuelRefueled = refuel.getAmount()/ refuel.getPrice();
        float refuelConsumption = (fuelRefueled*100)/traveledKms;
        Float averageConsumption = refuelService.getAverageConsumption(vehicleId);
        float medConsumption;
        if (averageConsumption != null){
            medConsumption = (averageConsumption+refuelConsumption)/2;
        }else {
            medConsumption = refuelConsumption;
        }
        if (traveledKms <= 0 ){
            throw new IllegalArgumentException("The current kilometers cannot be less than the previous refueling");
        }
        Optional<Vehicle> vehicle = vehicleService.findById(vehicleId);
        Optional<Station> station = stationService.findById(stationId);
        String stationName = null;
        String vehicleLicense = null;
        if (vehicle.isPresent() && station.isPresent()){
            Vehicle existVehicle = vehicle.get();
            Station existStation = station.get();
            stationName = existStation.getName();
            vehicleLicense = existVehicle.getLicensePlate();
            existVehicle.setKmActual(refuel.getKmTotal());
            existVehicle.setMedConsumption(medConsumption);
            vehicleService.save(existVehicle);
        } else {
            throw new VehicleNotFoundException("Vehicle or station not found");
        }
        refuel.setNameStation(stationName);
        refuel.setNameVehicle(vehicleLicense);
        refuel.setKmTraveled(traveledKms);
        refuel.setRefueledLiters(fuelRefueled);
        refuel.setRefuelConsumption(refuelConsumption);
        refuel.setMedConsumption(medConsumption);
        refuelService.addRefuel(vehicleId, stationId, refuel);
        return new ResponseEntity<>(refuel, HttpStatus.CREATED);
    }

    @DeleteMapping("/refuels/{id}")
    public ResponseEntity<Void> deleteRefuel(@PathVariable Long id) {
        refuelService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    // Control de excepciones
    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ErrorResponse> vehicleNotFoundException(VehicleNotFoundException unfe) {
        ErrorResponse errorResponse = ErrorResponse.generalError(404, unfe.getMessage());
        logger.error(unfe.getMessage(), unfe);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

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
    @ExceptionHandler(RefuelNotFoundException.class)
    public ResponseEntity<ErrorResponse> refuelNotFoundException(RefuelNotFoundException pnfe) {
        ErrorResponse errorResponse = ErrorResponse.generalError(404, pnfe.getMessage());
        logger.error(pnfe.getMessage(), pnfe);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }


}
