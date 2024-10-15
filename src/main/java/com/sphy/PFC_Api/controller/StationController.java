package com.sphy.PFC_Api.controller;


import com.sphy.PFC_Api.exception.StationNotFoundException;
import com.sphy.PFC_Api.exception.VehicleNotFoundException;
import com.sphy.PFC_Api.model.Station;
import com.sphy.PFC_Api.service.StationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class StationController {

    @Autowired
    private StationService stationService;

    // Obtener todas las estaciones
    @GetMapping("/stations")
    public ResponseEntity<List<Station>> getAllStations() {
        List<Station> stations = stationService.getAll();
        return new ResponseEntity<>(stations, HttpStatus.OK);
    }

    // Obtener una estación por ID
    @GetMapping("(/stations)/{id}")
    public ResponseEntity<Station> getStationById(@PathVariable Long id) throws StationNotFoundException {
        Optional<Station> optionalStation = stationService.findById(id);
        Station station = optionalStation.orElseThrow(() -> new StationNotFoundException(id));
        return new ResponseEntity<>(station, HttpStatus.OK);
    }

    // Crear una nueva estación
    @PostMapping("/stations")
    public ResponseEntity<Station> createStation(@Validated @RequestBody Station station) {
        Station savedStation = stationService.save(station);
        return new ResponseEntity<>(savedStation, HttpStatus.CREATED);
    }

    // Modificar una estación existente
    @PutMapping("(/stations)/{id}")
    public ResponseEntity<Station> modifyStation(@Valid @RequestBody Station station, @PathVariable long id) throws StationNotFoundException {

        Optional<Station> optionalStation = stationService.findById(id);
        if (optionalStation.isPresent()) {
            stationService.modifyStationById(station, id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new VehicleNotFoundException("Vehicle with license plate " + id + " not found.");
        }
    }

    // Eliminar una estación
    @DeleteMapping("(/stations)/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) throws StationNotFoundException {
        stationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
