package com.sphy.PFC_Api.service;



import com.sphy.PFC_Api.exception.StationNotFoundException;
import com.sphy.PFC_Api.exception.VehicleNotFoundException;
import com.sphy.PFC_Api.model.Refuel;
import com.sphy.PFC_Api.model.Station;
import com.sphy.PFC_Api.model.Vehicle;
import com.sphy.PFC_Api.repository.RefuelRepository;
import com.sphy.PFC_Api.repository.StationRepository;
import com.sphy.PFC_Api.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RefuelService {

    @Autowired
    private RefuelRepository refuelRepository;

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private StationService stationService;

    public Refuel addRefuel(String licensePlate, Refuel refuel) throws VehicleNotFoundException, StationNotFoundException {
        Refuel refuelAdd = new Refuel();

        Vehicle vehicle = vehicleService.findByLicensePlate(licensePlate).orElseThrow(() -> new VehicleNotFoundException(licensePlate));
        refuelAdd.setVehicle(vehicle);

        Station station = stationService.findById(refuel.getStationId())
                .orElseThrow(() -> new StationNotFoundException(refuel.getStationId()));

        refuelAdd.setStationId(station.getId());
        //refuelAdd.setStation(station);

        refuelAdd.setFuel(refuel.getFuel());
        refuelAdd.setAmount(refuel.getAmount());
        refuelAdd.setPrice(refuel.getPrice());
        refuelAdd.setCreationDate(refuel.getCreationDate());
        refuelRepository.save(refuelAdd);

        return refuelAdd;
    }

    public List<Refuel> getAll() {
        return refuelRepository.findAll();
    }

    public List<Refuel> getRefuelsByVehicleId(Long vehicleId) {
        return refuelRepository.findByVehicleId(vehicleId);
    }

    public List<Refuel> getRefuelsByStationId(Long stationId) {
        return refuelRepository.findByStationId(stationId);
    }

    public void delete(Long id) {
        refuelRepository.deleteById(id);
    }
}
