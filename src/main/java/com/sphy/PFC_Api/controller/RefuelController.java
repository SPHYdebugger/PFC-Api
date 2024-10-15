package com.sphy.PFC_Api.controller;

import com.sphy.PFC_Api.exception.VehicleNotFoundException;
import com.sphy.PFC_Api.model.Refuel;
import com.sphy.PFC_Api.service.RefuelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/refuels")
@Validated
public class RefuelController {

    private final RefuelService refuelService;

    @Autowired
    public RefuelController(RefuelService refuelService) {
        this.refuelService = refuelService;
    }

    @PostMapping("/refuels/{licensePlate}")
    public ResponseEntity<Refuel> createRefuel(@PathVariable String licensePlate, @Valid @RequestBody Refuel refuel) {
        Refuel savedRefuel = refuelService.addRefuel(licensePlate, refuel);
        return new ResponseEntity<>(savedRefuel, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Refuel>> getAllRefuels() {
        List<Refuel> refuels = refuelService.getAll();
        return new ResponseEntity<>(refuels, HttpStatus.OK);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<Refuel>> getRefuelsByVehicleId(@PathVariable Long vehicleId) {
        List<Refuel> refuels = refuelService.getRefuelsByVehicleId(vehicleId);
        return new ResponseEntity<>(refuels, HttpStatus.OK);
    }

    @GetMapping("/station/{stationId}")
    public ResponseEntity<List<Refuel>> getRefuelsByStationId(@PathVariable Long stationId) {
        List<Refuel> refuels = refuelService.getRefuelsByStationId(stationId);
        return new ResponseEntity<>(refuels, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRefuel(@PathVariable Long id) {
        refuelService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
