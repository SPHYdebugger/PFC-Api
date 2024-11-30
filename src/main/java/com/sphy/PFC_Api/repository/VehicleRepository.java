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

    @Query(value = "SELECT v.id, v.license_Plate, v.brand, v.model, v.km_actual, v.registration_date, v.hide, v.fuel1," +
            " v.fuel2, v.med_consumption, v.user_id, v.med_consumption2 FROM vehicles v ORDER BY v.id DESC", nativeQuery = true)
    List<Vehicle> findAllOrdered();

    @Query(value = "SELECT v.id, v.license_Plate, v.brand, v.model, v.km_actual, v.registration_date, v.hide, v.fuel1," +
            " v.fuel2, v.med_consumption, v.user_id, v.med_consumption2 FROM vehicles v WHERE v.user_id = ?1 ORDER BY v.id DESC", nativeQuery = true)
    List<Vehicle> findByUserIdOrdered(long userId);
}
