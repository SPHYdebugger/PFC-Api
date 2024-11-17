package com.sphy.PFC_Api.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefuelDTO {
    private long id;
    private String fuel;
    private float amount;
    private float price;
    private String creationDate;
    private String licensePlate;
    private String stationName;
    private long userId;
}
