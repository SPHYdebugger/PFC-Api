package com.sphy.PFC_Api.repository;


import com.sphy.PFC_Api.model.Refuel;
import com.sphy.PFC_Api.model.Station;
import com.sphy.PFC_Api.model.Vehicle;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefuelRepository extends CrudRepository<Refuel, Long> {


    List<Refuel> findAll();
    Optional<Refuel> findById(long id);



    List<Refuel> findByVehicle(Vehicle vehicle);
    List<Refuel> findByStation(Station station);


    List<Refuel> findByVehicleId(Long vehicleId);
    List<Refuel> findByVehicleLicensePlate(String licensePlate);
    List<Refuel> findByStationId(Long stationId);
    List<Refuel> findByNameStation(String refuelIdentifier);

    @Modifying
    @Transactional
    void deleteByVehicleId(Long vehicleId);

    @Modifying
    @Transactional
    void deleteByStationId(Long vehicleId);

    @Query(value = "SELECT vehicle_id FROM refuels WHERE id = :id", nativeQuery = true)
    Integer findVehicleId(@Param("id") long id);

    @Query(value = "SELECT AVG(med_consumption) FROM refuels WHERE vehicle_id = :vehicleId", nativeQuery = true)
    Float findAverageMedConsumptionByVehicleId(@Param("vehicleId") long vehicleId);

    @Query(value = "SELECT km_actual FROM vehicles WHERE id = :id", nativeQuery = true)
    Integer findTotalKmsByVehicleId(@Param("id") long id);

    /*@Modifying
    @Query("DELETE FROM refuels WHERE station_id = :stationId")
    void deleteByStationId(@Param("stationId") Long stationId);*/


}