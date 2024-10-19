package com.sphy.PFC_Api.service;



import com.sphy.PFC_Api.dto.VehicleDTO;
import com.sphy.PFC_Api.model.Vehicle;
import com.sphy.PFC_Api.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Vehicle> getAll() {
        return vehicleRepository.findAll();
    }
    public Optional<Vehicle> findById(long id) {
        return vehicleRepository.findById(id);
    }
    public Optional<Vehicle> findByLicensePlate(String licensePlate) {
        return vehicleRepository.findByLicensePlate(licensePlate);
    }
    public boolean existsById(Long vehicleId) {
        return vehicleRepository.existsById(vehicleId);
    }



    public Vehicle save(Vehicle vehicle) {
        return  vehicleRepository.save(vehicle);
    }


    public Vehicle modifyVehicleByLicense(VehicleDTO vehicleDTO, String licensePlate) {
        Optional<Vehicle> optionalVehicle = findByLicensePlate(licensePlate);
        if (optionalVehicle.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            vehicle.setBrand(vehicleDTO.getBrand());
            vehicle.setModel(vehicleDTO.getModel());
            vehicle.setFuel1(vehicleDTO.getFuel1());
            vehicle.setFuel2(vehicleDTO.getFuel2());
            vehicle.setKmActual(vehicleDTO.getKmInit());
            return vehicleRepository.save(vehicle);
        }
        return null;
    }





    public void deleteVehicleById(long id) {
        vehicleRepository.deleteById(id);
    }

    public void deleteVehicleByLicensePlate(String licensePlate) {
        vehicleRepository.deleteByLicensePlate(licensePlate);
    }



}