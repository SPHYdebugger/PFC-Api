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
        return vehicleRepository.findAllOrdered();
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


    public Vehicle modifyVehicleByLicense(Vehicle vehicle, String licensePlate) {
        System.out.println("vehiculo enviado como cuerpo; " + vehicle);
        Optional<Vehicle> optionalVehicle = findByLicensePlate(licensePlate);
        System.out.println("vehiculo encontrado con esa matricula; " + optionalVehicle.get());
        if (optionalVehicle.isPresent()) {
            Vehicle vehicleTemp = optionalVehicle.get();

            vehicleTemp.setBrand(vehicle.getBrand());
            vehicleTemp.setModel(vehicle.getModel());
            vehicleTemp.setFuel1(vehicle.getFuel1());
            vehicleTemp.setFuel2(vehicle.getFuel2());
            vehicleTemp.setKmActual(vehicle.getKmActual());
            vehicleTemp.setMedConsumption(vehicle.getMedConsumption());
            vehicleTemp.setRegistrationDate(vehicle.getRegistrationDate());
            vehicleTemp.setHide(vehicle.isHide());
            return vehicleRepository.save(vehicleTemp);
        }
            return null;
    }






    public void deleteVehicleById(long id) {
        vehicleRepository.deleteById(id);
    }

    public void deleteVehicleByLicensePlate(String licensePlate) {
        vehicleRepository.deleteByLicensePlate(licensePlate);
    }

    public Integer countRefuelsByVehicleId(long id) {
        return vehicleRepository.countRefuelsByVehicleId(id);
    }


    public List<Vehicle> getVehiclesByUser_id(long userId) { return vehicleRepository.findByUserIdOrdered(userId);}
}