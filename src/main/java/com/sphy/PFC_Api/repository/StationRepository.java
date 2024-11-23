package com.sphy.PFC_Api.repository;


import com.sphy.PFC_Api.model.Station;
import com.sphy.PFC_Api.model.Vehicle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StationRepository extends CrudRepository<Station, Long> {

    List<Station> findAll();
    Optional<Station> findByName(String name);
    List<Station> findByGlpFuel(boolean glpFuel);
    List<Station> findByFavorite(boolean Favorite);

    Optional<Station> deleteById(long stationId);

    @Query(value = "SELECT COUNT(*) FROM refuels WHERE station_id = :stationId", nativeQuery = true)
    int countRefuelsByStationId(@Param("stationId") long stationId);

    @Query(value = "SELECT s.id, s.name, s.address, s.site, s.province, s.registration_date, s.favorite, s.glp_fuel, s.hide, s.user_id " +
            "FROM stations s WHERE s.user_id = ?1 ORDER BY s.id DESC", nativeQuery = true)
    List<Station> findAllStationsOrderedByUserId(long userId);


}
