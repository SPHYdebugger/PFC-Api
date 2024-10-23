package com.sphy.PFC_Api.service;



import com.sphy.PFC_Api.dto.RefuelDTO;
import com.sphy.PFC_Api.exception.RefuelNotFoundException;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class RefuelService {

    @Autowired
    private RefuelRepository refuelRepository;

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private StationService stationService;


    public List<RefuelDTO> getAllRefuels() {
        List<Refuel> refuels = refuelRepository.findAll();
        return refuels.stream()
                .map(refuel -> new RefuelDTO(
                        refuel.getId(),
                        refuel.getFuel(),
                        refuel.getAmount(),
                        refuel.getPrice(),
                        refuel.getCreationDate().toString(),
                        refuel.getVehicle().getLicensePlate(),
                        refuel.getStation().getName()
                ))
                .collect(Collectors.toList());
    }

    /*public RefuelDTO getRefuelById(long refuelId) {
        Refuel refuel = refuelRepository.findById(refuelId)
                .orElseThrow(() -> new RefuelNotFoundException("Refuel with ID " + refuelId + " not found"));
        return new RefuelDTO(
                refuel.getId(),
                refuel.getFuel(),
                refuel.getAmount(),
                refuel.getPrice(),
                refuel.getCreationDate(),
                refuel.getVehicle().getLicensePlate(),
                refuel.getStation().getName()
        );
    }*/

    public Optional<Refuel> findById(long refuelId) {
        return refuelRepository.findById(refuelId);
    }

    public Integer findVehicleId(long id) {
        return refuelRepository.findVehicleId(id);
    }

    public List<Refuel> getRefuelsByVehicleId(Long vehicleId) {
        return refuelRepository.findByVehicleId(vehicleId);
    }
    public List<Refuel> getRefuelsByLicensePlate(String licensePlate) {
        return refuelRepository.findByVehicleLicensePlate(licensePlate);
    }

    public List<Refuel> getRefuelsByStationId(Long stationId) {
        return refuelRepository.findByStationId(stationId);
    }
    public List<Refuel> getRefuelsByStationName(String refuelIdentifier) {
        return refuelRepository.findByNameStation(refuelIdentifier);
    }


    public Refuel addRefuel(long vehicleId, long stationId, Refuel refuel) {
        Vehicle vehicle = vehicleService.findById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException(" The vehicleId " + vehicleId + " doesn´t exist"));
        Station station = stationService.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException(stationId));
        refuel.setVehicle(vehicle);
        refuel.setStation(station);
        refuel.setCreationDate(LocalDate.now());
        return refuelRepository.save(refuel);
    }



    //Borrado
    public void delete(Long id) {
        refuelRepository.deleteById(id);
    }
    public void deleteRefuelsByVehicleId(Long vehicleId) {
        refuelRepository.deleteByVehicleId(vehicleId);
    }
    public void deleteRefuelsByStationId(Long stationId) {refuelRepository.deleteByStationId(stationId);}


    public Integer getTotalKmsByVehicleId(long id) {
        return refuelRepository.findTotalKmsByVehicleId(id);
    }
    public Float getAverageConsumption(long id) {
        return refuelRepository.findAverageMedConsumptionByVehicleId(id);
    }


    public void save(Refuel refuel) {
        refuelRepository.save(refuel);
    }
}
