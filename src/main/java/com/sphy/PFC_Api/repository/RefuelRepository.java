package com.sphy.PFC_Api.repository;


import com.sphy.PFC_Api.model.Refuel;
import com.sphy.PFC_Api.model.Station;
import com.sphy.PFC_Api.model.Vehicle;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefuelRepository extends CrudRepository<Refuel, Long> {


    List<Refuel> findAll();
    Optional<Refuel> findById(long id);

    List<Refuel> findByCreationDate(String date);

    List<Refuel> findByVehicle(Vehicle vehicle);
    List<Refuel> findByStation(Station station);


    List<Refuel> findByVehicleId(Long vehicleId);
    List<Refuel> findByVehicleLicensePlate(String licensePlate);
    List<Refuel> findByStationId(Long stationId);

    @Modifying
    @Transactional
    void deleteByVehicleId(Long vehicleId);



}