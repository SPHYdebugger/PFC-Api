package com.sphy.PFC_Api.service;


import com.sphy.PFC_Api.exception.StationNotFoundException;
import com.sphy.PFC_Api.model.Station;
import com.sphy.PFC_Api.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StationService {

    @Autowired
    private StationRepository stationRepository;


    public List<Station> getAll() {
        return stationRepository.findAll();
    }


    public Optional<Station> findById(Long id) {
        return stationRepository.findById(id);
    }

    // Método para guardar una nueva estación
    public Station save(Station station) {
        return stationRepository.save(station);
    }

    // Método para actualizar una estación existente
    public void modifyStationById(Station newStation, long id) {
        Optional<Station> station = stationRepository.findById(id);
        if (station.isPresent()) {
            Station existingStation = station.get();
            existingStation.setName(newStation.getName());
            existingStation.setAddress(newStation.getAddress());
            existingStation.setFavorite(newStation.isFavorite());
            existingStation.setGlpFuel(newStation.isGlpFuel());
            stationRepository.save(existingStation);
        }
    }

    // Método para eliminar una estación
    public void delete(Long id) throws StationNotFoundException {
        if (!stationRepository.existsById(id)) {
            throw new StationNotFoundException(id);
        }
        stationRepository.deleteById(id);
    }
}
