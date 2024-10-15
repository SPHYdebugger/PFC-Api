package com.sphy.PFC_Api.repository;


import com.sphy.PFC_Api.model.Station;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StationRepository extends CrudRepository<Station, Long> {

    List<Station> findAll();
    List<Station> findByGlpFuel(boolean glpFuel);
    List<Station> findByFavorite(boolean Favorite);

    Optional<Station> deleteById(long stationId);



}
