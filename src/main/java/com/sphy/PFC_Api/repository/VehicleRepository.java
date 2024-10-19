package com.sphy.PFC_Api.repository;


import com.sphy.PFC_Api.model.Vehicle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, Long> {
    List<Vehicle> findAll();

    Optional<Vehicle> findById(long vehicleId);
    Optional<Vehicle> findByLicensePlate(String VehicleLicensePlate);
    //List<Vehicle> findByGlpFuel(boolean glpFuel);
    //List<Vehicle> findByUser(String userId);


    void deleteById(long vehicleId);
    void deleteByLicensePlate(String licensePlate);

    @Query(value = "SELECT COUNT(*) FROM refuels WHERE vehicle_id = :vehicleId", nativeQuery = true)
    int countRefuelsByVehicleId(@Param("vehicleId") long vehicleId);


}
