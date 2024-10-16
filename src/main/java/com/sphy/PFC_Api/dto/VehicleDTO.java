package com.sphy.PFC_Api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    @NotNull(message = "Brand can't be NULL")
    @NotBlank(message = "Brand can't be empty")
    private String brand;

    @NotNull(message = "Model can't be NULL")
    @NotBlank(message = "Model can't be empty")
    private String model;

    @NotNull(message = "Fuel1 can't be NULL")
    @NotBlank(message = "Fuel1 can't be empty")
    private String fuel1;


    private String fuel2;

    @NotNull(message = "Initial kilometers can´t be NULL")
    @Positive(message = "Initial Kilometers can´t be negative")
    private int kmInit;
}
