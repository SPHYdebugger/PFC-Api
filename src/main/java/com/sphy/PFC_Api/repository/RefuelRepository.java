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
    @Query(value = "SELECT r.id, r.name_station, r.name_vehicle, r.fuel, r.amount, r.price, r.km_total, r.km_traveled, r.fulled, " +
            "r.creation_date, r.refuel_consumption, r.refueled_liters, r.med_consumption, r.station_id, r.vehicle_id, r.user_id " +
            "FROM refuels r " +
            "WHERE r.vehicle_id = :vehicleId " +
            "ORDER BY r.id DESC", nativeQuery = true)
    List<Refuel> findRefuelsByVehicleIdOrdered(@Param("vehicleId") String vehicleId);

    List<Refuel> findByVehicleLicensePlate(String licensePlate);
    @Query(value = "SELECT r.id, r.name_station, r.name_vehicle, r.fuel, r.amount, r.price, r.km_total, r.km_traveled, r.fulled, " +
            "r.creation_date, r.refuel_consumption, r.refueled_liters, r.med_consumption, r.station_id, r.vehicle_id, r.user_id, r.double_refuel, " +
            "r.second_fuel, r.second_amount, r.kms_traveled_second_fuel, r.second_fulled, r.second_med_consumption, r.second_price," +
            "r.second_refuel_consumption, r.second_refueled_liters FROM refuels r " +
            "WHERE r.name_vehicle = :vehicleName " +
            "ORDER BY r.creation_date DESC", nativeQuery = true)
    List<Refuel> findRefuelsByVehicleNameOrdered(@Param("vehicleName") String vehicleName);


    List<Refuel> findByStationId(Long stationId);
    @Query(value = "SELECT r.id, r.name_station, r.name_vehicle, r.fuel, r.amount, r.price, r.km_total, r.km_traveled, r.fulled," +
            "r.creation_date, r.refuel_consumption, r.refueled_liters, r.med_consumption, r.station_id, r.vehicle_id, r.user_id, r.double_refuel," +
            "r.second_fuel, r.second_amount, r.kms_traveled_second_fuel, r.second_fulled, r.second_med_consumption, r.second_price," +
            "r.second_refuel_consumption, r.second_refueled_liters FROM refuels r " +
            "WHERE r.station_id = :stationId " +
            "ORDER BY r.id DESC", nativeQuery = true)
    List<Refuel> findRefuelsByStationIdOrdered(@Param("stationId") String stationId);


    List<Refuel> findByNameStation(String refuelIdentifier);
    @Query(value = "SELECT r.id, r.name_station, r.name_vehicle, r.fuel, r.amount, r.price, r.km_total, r.km_traveled, r.fulled, " +
            "r.creation_date, r.refuel_consumption, r.refueled_liters, r.med_consumption, r.station_id, r.vehicle_id, r.user_id, r.double_refuel," +
            "r.second_fuel, r.second_amount, r.kms_traveled_second_fuel, r.second_fulled, r.second_med_consumption, r.second_price," +
            "r.second_refuel_consumption, r.second_refueled_liters FROM refuels r " +
            "WHERE r.name_station = :stationName " +
            "ORDER BY r.creation_date DESC", nativeQuery = true)
    List<Refuel> findRefuelsByStationNameOrdered(@Param("stationName") String stationName);

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

    @Query(value = "SELECT AVG(second_med_consumption) FROM refuels WHERE vehicle_id = :vehicleId", nativeQuery = true)
    Float findAverageMedConsumption2ByVehicleId(@Param("vehicleId") long vehicleId);

    @Query(value = "SELECT km_actual FROM vehicles WHERE id = :id", nativeQuery = true)
    Integer findTotalKmsByVehicleId(@Param("id") long id);

    /*@Modifying
    @Query("DELETE FROM refuels WHERE station_id = :stationId")
    void deleteByStationId(@Param("stationId") Long stationId);*/

    @Query(value = "SELECT r.id, r.name_station, r.name_vehicle, r.fuel, r.amount, r.price, r.km_total, r.km_traveled, r.fulled, " +
            "r.creation_date, r.refuel_consumption, r.refueled_liters, r.med_consumption, r.station_id, r.vehicle_id " +
            "FROM Refuel r ORDER BY r.id DESC", nativeQuery = true)
    List<Refuel> findAllRefuelsOrdered();


}