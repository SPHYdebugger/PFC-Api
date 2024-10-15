package com.sphy.PFC_Api.service;



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


    /*public List<Vehicle> findVehicleByUser(String firstname) {
        return vehicleRepository.findByUser(firstname);
    }*/


    public void save(Vehicle vehicle) { vehicleRepository.save(vehicle);
    }
    @Transactional
    public void deleteVehicleById(long id) {
        vehicleRepository.deleteById(id);
    }
    @Transactional
    public void deleteVehicleByLicensePlate(String liceseplate) {
        vehicleRepository.deleteByLicensePlate(liceseplate);
    }

    public void modifyVehicleByLicense(Vehicle newVehicle, String licensePlate) {
        Optional<Vehicle> vehicle = vehicleRepository.findByLicensePlate(licensePlate);
        if (vehicle.isPresent()) {
            Vehicle existingVehicle = vehicle.get();
            existingVehicle.setBrand(newVehicle.getBrand());
            existingVehicle.setModel(newVehicle.getModel());
            existingVehicle.setFuel1(newVehicle.getFuel1());
            existingVehicle.setFuel2(existingVehicle.getFuel2());
            existingVehicle.setKmInit(newVehicle.getKmInit());
            vehicleRepository.save(existingVehicle);
        }
    }



}